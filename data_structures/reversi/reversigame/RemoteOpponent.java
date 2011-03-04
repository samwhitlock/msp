package reversigame;

import java.rmi.RemoteException;

import ucb.util.mailbox.Mailbox;
import ucb.util.mailbox.QueuedMailbox;

import static reversigame.Command.*;

/** A Contestant that receives its moves a remote program.
 * @author Sam Whitlock (cs61b-eo)
 * */
class RemoteOpponent extends Contestant {

	/** A Contestant that will play MYCOLOR on BOARD, taking its move from
	 *  a remote opponent that deposits its moves into FROMREMOTE and
	 *  that retrieves moves that I send from TOREMOTE. */
	RemoteOpponent (Mailbox<String> fromRemote, Mailbox<String> toRemote,
			Board board, PieceColor myColor, boolean depositFirstMove) {
		super (board, myColor);
		this.fromRemote = fromRemote;
		this.toRemote = toRemote;
		depositLastMove = depositFirstMove;
	}

	/** Assuming that the game is not over and it is my turn, make a
	 *  valid move on the board, after sending the last move to the
	 *  remote opponent, if necessary. */
	void takeMove () {
		try {
			if( board.resigned() ) {
				toRemote.deposit("resign");
				String s1 = fromRemote.receive();
				if( s1.compareTo("") != 0) {
					System.err.println("error : bad resignation handshake | " + s1);
				}
				fromRemote.close();
				toRemote.close();
			} else if( board.gameOver() ) {
				toRemote.deposit(board.lastMove());
				String s1 = fromRemote.receive();
				if( s1.compareTo("") != 0) {
					System.err.println("error : bad last move handshake | " + s1);
				}
				fromRemote.close();
				toRemote.close();
			} else {
				if( depositLastMove ) {
					toRemote.deposit(board.lastMove());
				}
				String s = fromRemote.receive();

				if( s == null ) {
					board.resign();
				} else {
					String[] opnd = new String[2];
					Command c = Command.parseCommand(s, opnd);
					if(c==PIECEMOVE) {
						if( !board.makeMove(opnd[0]) ) {
							System.err.println("error : bad move sent from remote opponent | " + opnd[0]);
							board.resign(myColor);
							toRemote.close();
							fromRemote.close();
						} else {
							if( board.gameOver() ) {
								toRemote.deposit("");
							}
							depositLastMove = true;
						}
					} else if(c==PASS) {
						if( !board.pass() ) {
							System.err.println("error : bad pass move sent from remote opponent");
							board.resign(myColor);
							toRemote.close();
							fromRemote.close();
						} else {
							depositLastMove = true;
						}
					} else if(c==RESIGN) {
						board.resign(myColor);
						toRemote.deposit("");
					} else {
						System.err.println("error : illegal command sent from remote opponent");
						board.resign(myColor);
						fromRemote.close();
						toRemote.close();
					}
				}
			}
		} catch (RemoteException e) {
			board.resign(myColor);
			fromRemote = toRemote = null;
		} catch (InterruptedException e) {
			System.exit(1);
		}
	}

	Mailbox<String> fromRemote, toRemote;

	/**A boolean to control whether the last move should be deposited
	 * This really only comes into play for the first move
	 * @author Sam Whitlock (cs61b-eo)
	 */
	private boolean depositLastMove = false;
}

