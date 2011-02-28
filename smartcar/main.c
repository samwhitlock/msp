/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include "io430.h"

/* key to ports
* Left Motor (P10.(6,7))
**Forward: 6 high, 7 low
**Reverse: 6 low, 7 high
* Right Motor (P10.(0,3))
**Forward: 0 high, 3 low
**Reverse: 0 low, 3 high
* Left Front Sensor: P7.6
* Right Front Sensor: P7.5
* Left Rear Sensor: P6.7
* Right Rear Sensor: P7.4
*/
void stop();
void delay(unsigned int n);
void forward();
void reverse();
void accelforward();
void decelforward();
void accelreverse();
void decelreverse();
void pivotForwardAroundLeft();
void pivotBackwardAroundLeft();
void pivotForwardAroundRight();
void pivotBackwardAroundRight();
int cancelExecution();
void ledSetup(int n);
void sense();
void respondLeft();
void respondRight();
int determineRearCase();
void longDelay(unsigned int n);

void longDelay(unsigned int n) {
  for( ; n > 0; n-- ) {
    delay(65000);
  }
}

int main( void )
{
  // Stop watchdog timer to prevent time out reset
  WDTCTL = WDTPW + WDTHOLD;
  
  //Initialize the movement parameters
  P10OUT &= ~(BIT4+BIT5+BIT6+BIT7); // P10.0-3 low
  P10SEL = 0x00; // All of port 10 set up for digital I/O
  P10DIR |= BIT4+BIT5+BIT6+BIT7; // configure P10.0 as output
  
  //set up LEDs
  P1SEL &= ~(BIT1 + BIT0); // configure P1.0 & P1.1 for digital IO
  P1DIR |= BIT0 + BIT1;
  
  ledSetup(1);
  
  P6OUT |= BIT7;
  P6DIR &= ~BIT7;
  P6REN |= BIT7;
  
  P7OUT |= BIT4+BIT5+BIT6;
  P7DIR &= ~BIT4+BIT5+BIT6;
  P7REN |= BIT4+BIT5+BIT6;
  
  //setup the button
  P2OUT = BIT6;
  P2DIR = ~BIT6;
  P2REN = BIT6;

  while( P2IN & BIT6 );
  
  ledSetup(0);
  
  accelforward();
  
  while( P2IN & BIT6 ) {
    sense();
  }
  
  stop();
  
  return 0;
}

void ledSetup(int n) {
  if( n == 0 ) {
    P1OUT &= ~(BIT0 + BIT1);
  } else if( n == 1 ) {
    ////waiting for start button to be presssed
    P1OUT &= ~BIT0;
    P1OUT |= BIT1;
  } else if( n == 2 ) {
    //object avoidance activated
    P1OUT &= ~BIT1;
    P1OUT |= BIT0;
  } else if (n == 3 ) {
    //rear AI activated
    P1OUT |= BIT0 + BIT1;
  }
}

void delay(unsigned int n) {
  while (n > 0) n--;
}

void forward() {
  stop();
  P10OUT |= BIT4+BIT6;
}

void reverse() {
  stop();
  P10OUT |= BIT5+BIT7;
}

void accelforward() {
  stop();
  for(unsigned int x = 6000; x > 0; x -= 100 ) {
    stop();
    delay(x);
    P10OUT |= BIT4+BIT6;
    delay(6000 - x);
  }
  P10OUT |= BIT4+BIT6;
}

void decelforward() {
  for(unsigned int x = 6000; x > 0; x -= 100 ) {
    stop();
    delay(6000-x);
    P10OUT |= BIT4+BIT6;
    delay(x);
  }
  stop();
}

void accelreverse() {
  stop();
  for(unsigned int x = 6000; x > 0; x -= 100 ) {
    stop();
    delay(x);
  P10OUT |= BIT5+BIT7;
    delay(6000 - x);
  }
  P10OUT |= BIT5+BIT7;
}

void decelreverse() {
  for(unsigned int x = 6000; x > 0; x -= 100 ) {
    stop();
    delay(6000 - x);
  P10OUT |= BIT5+BIT7;
    delay(x);
  }
  stop();
}

void rotateLeft() {
  stop();
  P10OUT |= BIT4+BIT7;
}

void rotateRight() {
  stop();
  P10OUT |= BIT5+BIT6;
}

void pivotForwardAroundLeft() {
  stop();
  P10OUT |= BIT4;
}

void pivotBackwardAroundLeft() {
  stop();
  P10OUT |= BIT5;
}

void pivotForwardAroundRight() {
  stop();
  P10OUT |= BIT6;
}

void pivotBackwardAroundRight() {
  stop();
  P10OUT |= BIT7;
}

void stop() {
  P10OUT &= ~(BIT4+BIT5+BIT6+BIT7);
}

void sense() {
  if( ~P7IN & BIT5 ) {
    ledSetup(2);
    respondLeft();
    ledSetup(0);
  } else if( ~P7IN & BIT6 ) {
    ledSetup(2);
    respondRight();
    ledSetup(0);
  }
}

void respondRight() {
  accelreverse();
  int rearCase = 0;
  for( unsigned int time = 35000; time > 0; time-- ) {
    rearCase = determineRearCase();
    switch(rearCase) {
    case 0:
      break;
    case 1://left sensed
      ledSetup(3);
      pivotForwardAroundLeft();
      longDelay(3);
      accelforward();
      return;
    case 2://right sensed
      ledSetup(3);
      rotateLeft();
      longDelay(3);
      accelforward();
      return;
    }
  }
  
  rotateLeft();
  longDelay(7);
  
  accelforward();
}

void respondLeft() {
  accelreverse();
  int rearCase = 0;
  for( unsigned int time = 35000; time > 0; time-- ) {
    rearCase = determineRearCase();
    switch(rearCase) {
    case 0:
      break;
    case 1://left sensed activated
      ledSetup(3);
      rotateRight();
      longDelay(3);
      accelforward();
      return;
    case 2://right sensed
      ledSetup(3);
      pivotForwardAroundRight();
      longDelay(3);
      accelforward();
      return;
    }
  }
  
  rotateRight();
  longDelay(7);
  
  accelforward();
}

int determineRearCase() {
  int retInt = 0;
  if( ~P6IN & BIT7 ) {
    retInt += 1;
  } else if( ~P7IN & BIT4 ) {
    retInt += 2;
  }
  return retInt;
}
