import scala.util.Random

case class Deck(cards: List[Card]):

  /**
   * Returns a new Deck with the cards shuffled.
   * This is immutable - it does not change the original deck.
   */
  def shuffle: Deck =
    Deck(Random.shuffle(this.cards))

  /**
   * Deals one card.
   * Returns an Option containing a tuple of (the dealt card, the rest of the deck).
   * Returns None if the deck is empty.
   */
  def deal: Option[(Card, Deck)] =
    this.cards match
      case Nil => None // No cards left
      case head :: tail => Some((head, Deck(tail)))

// Use a companion object to create a factory for a standard 52-card deck.
object Deck:
  def standard52CardDeck: Deck =
    val allCards = for {
      suit <- Suit.values
      rank <- Rank.values
    } yield Card(rank, suit)
    Deck(allCards.toList)