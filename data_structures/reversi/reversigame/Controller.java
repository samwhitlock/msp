package reversigame;

import java.util.ArrayList;
import java.util.Random;

import ucb.util.SimpleObjectRegistry;
import ucb.util.mailbox.Mailbox;
import ucb.util.mailbox.QueuedMailbox;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import static reversigame.PieceColor.*;
import static reversigame.Command.*;

/** A Controller of the overall game logic: determines who plays when,
 * handles remote play, determines source of Player's input.
 * @author Sam Whitlock (cs61b-eo)
 */ 
abstract class Controller {

	boolean quitnow = false;
	/** Read a line of input from System.in, removing comments and 
	 *  leading and trailing whitespace, and skipping blank lines.  
	 *  Returns null when input exhausted. */
	abstract String getLine ();

	/** Add the data from STREAM to the from of the current input
	 *  source.  I take over ownership of STREAM and am responsible
	 *  for closing it when done. */
	abstract void prepend (BufferedReader stream);

	/** Run a session of Reversi gaming. */
	@SuppressWarnings({ "unchecked", "unused" })
	void process () {
		Board board;
		Random rand;
		boolean playerIsHuman;

		board = new Board ();
		rand = new Random ();
		playerIsHuman = true;

		Mailbox<String> playerInbox, playerOutbox;

		GameLoop:
			while (true) {
				boolean clientMode = false, hostMode = false;
				Contestant player, opponent;
				PieceColor playersColor = BLACK;
				playerInbox = playerOutbox = null;

				board.clear ();

				PrePlayLoop:
					while (true) {
						String command = null;
						if (clientMode) {
							try {
								command = playerInbox.receive ();
								if( command == null ) {
									System.out.println("Opponent resigns due to communication failure");
									clientMode = false;
									command = getLine();
								}
							} catch (InterruptedException e) {
								System.exit(1);
							} catch (RemoteException e) {
								System.out.println("Opponent resigns due to communication failure");
								clientMode = false;
								command = getLine();
							}
						} else {
							command = getLine ();
						}
						String[] opnd = new String[2];

						Command c = Command.parseCommand(command, opnd);

						if(c==AUTO) {
							playerIsHuman = false;
						} else if(c==PRINT) {
							printBoard(board);
						} else if(c==MOVES) {
							printMoves(board.legalMoves());
						} else if(c==CLEAR) {
							board.clear();
							if(hostMode) {
								try {
									playerOutbox.deposit("clear");
								} catch (RemoteException e) {
									System.err.println("error : opponent resign due to connectivity failure");
									hostMode = false;
									playerInbox = playerOutbox = null;
								} catch (InterruptedException e) {
									System.exit(1);
								}
							}
						} else if(c==MANUAL) {
							playerIsHuman = true;
						} else if(c==COLOR) {
							playersColor = opnd[0].compareToIgnoreCase("black") == 0 ? BLACK : WHITE;
							if(hostMode) {
								try {
									playerOutbox.deposit("color " + playersColor.opposite().asString());
								} catch (RemoteException e) {
									System.err.println("error : opponent resign due to connectivity failure");
									hostMode = false;
									playerInbox = playerOutbox = null;
								} catch (InterruptedException e) {
									System.exit(1);
								}
							}
						} else if(c==HOST) {
							try {
								SimpleObjectRegistry reg = new SimpleObjectRegistry ();
								playerInbox = QueuedMailbox.<String>create (1);
								playerOutbox = QueuedMailbox.<String>create (1);
								reg.rebind (opnd[0] + ".IN", playerInbox);
								reg.rebind (opnd[0] + ".OUT", playerOutbox);
								System.out.println("Waiting for client to connect...");
								String s = playerInbox.receive();
								if( s.compareTo("") == 0 ) {
									hostMode = true;
									System.out.println("\nClient connected!");
									playerOutbox.deposit("clear");
									playerOutbox.deposit("color " + playersColor.opposite().asString());
									board.clear();
								} else {
									System.err.println("\nerror : Bad client handshake");
								}
								reg.close();
							} catch (RemoteException e) {
								System.err.println("error : Unable to host");
								playerInbox = playerOutbox = null;
							} catch (InterruptedException e) {
								System.exit(1);
							}
						} else if(c==JOIN) {
							System.out.println("Attempting to connect to host....");
							try {
								playerOutbox = (Mailbox<String>) SimpleObjectRegistry.findObject(opnd[0] + ".IN", opnd[1]);
								playerInbox = (Mailbox<String>) SimpleObjectRegistry.findObject(opnd[0] + ".OUT", opnd[1]);

								playerOutbox.deposit("");
								clientMode = true;
								System.out.println("\nSuccessfully connected to remote game with " + opnd[0] + "@" + opnd[1]);
								System.out.println("Host is configuring game setup");
								System.out.println("A prompt will appear once the game starts\n");
							} catch (NotBoundException e) {
								System.err.println("\nerror : Unable to connect to host");
								clientMode = false;
								playerInbox = playerOutbox = null;
							} catch (RemoteException e) {
								System.err.println("\nerror : Host resigns");
								clientMode = false;
								playerInbox = playerOutbox = null;
							} catch (InterruptedException e) {
								System.exit(1);
							}
						} else if(c==START) {
							System.out.println("Prepare for epic pwnage!");
							boolean bReak = false;
							try {
								if(hostMode) {
									playerOutbox.deposit("start");
									if( board.gameOver() ) {
										String s = playerInbox.receive();
										if( s.compareTo("") != 0 ) {
											System.err.println("error : unsuccessful handshake with remote opponent");
										}
									}
								} else if(clientMode && board.gameOver()) {
									playerOutbox.deposit("");
								}
								bReak = true;
							} catch (RemoteException e) {
								System.err.println("error : opponent resign due to connectivity failure");
								hostMode = false;
								playerInbox = playerOutbox = null;
							} catch (InterruptedException e) {
								System.exit(1);
							}
							if( bReak ) {
								break;
							}
						} else if(c==PIECEMOVE) {
							if( board.makeMove(opnd[0])) {
								if(hostMode) {
									try {
										playerOutbox.deposit(opnd[0]);
									} catch (RemoteException e) {
										System.err.println("error : opponent resign due to connectivity failure");
										hostMode = false;
										playerInbox = playerOutbox = null;
									} catch (InterruptedException e) {
										System.exit(1);
									}
								}
							} else {
								System.err.println("error : invalid piecemove");
							}
						} else if(c==PASS) {
							if(board.pass()) {
								if(hostMode) {
									try {
										playerOutbox.deposit("-");
									} catch (RemoteException e) {
										System.err.println("error : opponent resign due to connectivity failure");
										hostMode = false;
										playerInbox = playerOutbox = null;
									} catch (InterruptedException e) {
										System.exit(1);
									}
								}
							} else {
								System.err.println("error : Passing is not a valid move from the current board");
							}
						} else if(c==RESIGN) {
							System.err.println("error : You can only resign during a game");
						} else if(c==LOAD) {
							try {
								prepend(new BufferedReader(new FileReader(opnd[0])));
								System.out.println("File loading....");
							} catch (FileNotFoundException e) {
								System.err.println("error : file not found | " + opnd[0]);
							}
						} else if(c==HELP) {
							printHelp();
						} else if(c==DUMP) {
							dump(board);
						} else if(c==QUIT) {
							if(clientMode || hostMode) {
								try {
									playerOutbox.close();
									playerInbox.close();
								} catch (RemoteException e) {
								} catch (InterruptedException e) {
									System.exit(1);
								}
							}
							quit();
						} else if(c==SEED) {
							System.out.println("AI reseeded");
							if(hostMode) {
								try {
									playerOutbox.deposit(command);
								} catch (RemoteException e) {
									System.err.println("error : opponent resign due to connectivity failure");
									hostMode = false;
									playerInbox = playerOutbox = null;
								} catch (InterruptedException e) {
									System.exit(1);
								}
							}
						} else {
							System.err.println("error : unknown command or operands");
						}
					}

				if( playerIsHuman ) {
					player = new Human (this, board, playersColor);
				} else {
					player = new AI(board, playersColor);
				}
				if (clientMode || hostMode) 
					opponent = 
						new RemoteOpponent (playerInbox, playerOutbox, board,
								playersColor.opposite (), playersColor == board.whoseMove());
				else
					opponent = new AI (board, playersColor.opposite ());

				PlayLoop:
					while (!board.gameOver () ) {
						if (board.whoseMove () == player.myColor ()) {
							player.takeMove ();
							if( quitnow ) {
								if( hostMode || clientMode) {
									try {
										playerInbox.close();
										playerOutbox.close();
									} catch (RemoteException e) {
									} catch (InterruptedException e) {
										System.exit(1);
									}
								}
								quit();
							} else if( (clientMode || hostMode) && board.gameOver() ) {
								opponent.takeMove();
							}
						} else {
							opponent.takeMove ();
							if(board.resigned()) {
								break;
							}
						}
					}

				if( board.resigned() ) {
					if( board.resigned == BLACK ) {
						System.out.println("White wins.");
					} else {
						System.out.println("Black wins.");
					}
				} else {
					if(hostMode) {
						try {
							if(!playerOutbox.isClosed())
								playerOutbox.close();
							if(!playerInbox.isClosed())
								playerInbox.close();
						} catch (RemoteException e) {
							System.out.println("oh snap!");
						} catch (InterruptedException e) {
							System.exit(1);
						}
					}
					int gameOutcome = board.blackPieces() - board.whitePieces();
					if( gameOutcome > 0 ) {
						System.out.println("Black wins.");
					} else if( gameOutcome < 0 ) {
						System.out.println("White wins.");
					} else {
						System.out.println("Draw.");
					}
				}
			}
	}

	/**Prints the moves of a board based on on the input from the legalMoves
	 * method from the board
	 * @param ArrayList of legalMoves
	 * @author Sam Whitlokc (cs61b-eo)
	 * @return void
	 */
	static void printMoves(ArrayList<int[]> legalMoves) {
		for(int i = 0; i < legalMoves.size(); i++) {
			if( i % 10 == 0 && i > 0) {
				System.out.println();
			}
			int[] x = legalMoves.get(i);
			System.out.print(asString(x[0], x[1]) + " ");
		}
		System.out.println();
	}

	/**Returns a string that is representative of the move based on the 
	 * numerical inputs
	 * @param int column num
	 * @param int row num
	 * @return String (move)
	 * @author Sam Whitlock (cs61b-eo)
	 */
	private static String asString(int c, int r) {
		if( c >= 0 && c < 8 && r >= 0 && r < 8) {
			return Board.numberToLetterMap[c] + (1 + r);
		} else return "";
	}

	/**Prints the board to the command line as indicated by the spec
	 * @author Sam Whitlock (cs61b-eo)
	 * @param board
	 * @return void
	 */
	static void dump(Board board) {
		if(board != null) {
			System.out.println("===");
			for( int i = 0; i < 8; i++ ) {
				System.out.print(" ");
				for( int j = 0; j < 8; j++ ) {
					System.out.print(" " + board.get(j, i).pieceChar());
				}
				System.out.println();
			}
			System.out.println("User: " + board.whoseMove());
			System.out.println("Moves: " + board.numMoves());
			System.out.println("===");
		}
	}

	/**Prints the board with column and row markers
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Board
	 * @return void
	 */
	public static void printBoard(Board board) {
		if( board != null ) {
			System.out.println("  a b c d e f g h");
			for( int i = 0; i < 8; i++ ) {
				System.out.print(i + 1);
				for( int j = 0; j < 8; j++ ) {
					System.out.print(" " + board.get(j, i).pieceChar());
				}
				System.out.println();
			}
			System.out.println("User: " + board.whoseMove());
			System.out.println("Moves: " + board.numMoves());
		}
	}

	/**Prints the help prompt at the standard output
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	static void printHelp() {
		System.out.println("Commands:");
		System.out.println("\tquit\t\tExit the game");
		System.out.println("\tclear\t\tReset the board to the initial setup");
		System.out.println("\tstart\t\tBegin the game");
		System.out.println("\tload FILE\t\tRead commands from FILE");
		System.out.println("\tdump\t\tDumps the current game state");
		System.out.println("\tauto\t\tMakes Player an AI");
		System.out.println("\tmanual\t\tUser enters Player's moves");
		System.out.println("\tseed N\t\tSet seed for random numbers");
		System.out.println("\tcolor (white|black)\tSet Player's color");
		System.out.println("\thost ID\t\tWait for someone to join game named ID");
		System.out.println("\tjoin ID@HOST\tJoin game named ID on HOST");
		System.out.println("\tCR\tPiecemove");
		System.out.println("\t\tMove to square CR (C = [a-hA-H], R = 1-8");
		System.out.println("\t-\t\tPass (only legal when no moves are possible)");
		System.out.println("\tresign\t\tresign from current gain and reenter setup");
		System.out.println("\thelp\t\tthis message");
	}

	/**Prints the exit dialogue at the standard output and exits the program with status 0
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	void quit() {
		System.out.println("Thank you for playing Reversi!");
		System.out.println("If you liked it, please consider making a donation, ");
		System.out.println("for I spent a lot of my own cold hard cash on coffee ");
		System.out.println("to make this game for you.");
		System.out.println("PayPal and credit cards aren't accepted, but my email is:");
		System.out.println("cs61b-eo@imail.eecs.berkeley.edu");
		System.exit(0);
	}
}

