// Using enum for a fixed set of suits
enum Suit(val symbol: Char):
  case Hearts extends Suit('♥')
  case Diamonds extends Suit('♦')
  case Clubs extends Suit('♣')
  case Spades extends Suit('♠')

// Enum for card ranks. We add the game logic for value directly here.
enum Rank(val display: String, val value: Int):
  case Two extends Rank("2", 2)
  case Three extends Rank("3", 3)
  case Four extends Rank("4", 4)
  case Five extends Rank("5", 5)
  case Six extends Rank("6", 6)
  case Seven extends Rank("7", 7)
  case Eight extends Rank("8", 8)
  case Nine extends Rank("9", 9)
  case Ten extends Rank("10", 10)
  case Jack extends Rank("J", 10)
  case Queen extends Rank("Q", 10)
  case King extends Rank("K", 10)
  // Ace is initially valued at 11. The game logic will handle reducing it to 1.
  case Ace extends Rank("A", 11)

case class Card(rank: Rank, suit: Suit):
// Override toString for a nice, clean print output like "A♠"
  override def toString: String = s"${rank.display}${suit.symbol}"