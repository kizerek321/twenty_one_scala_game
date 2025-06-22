import scala.io.StdIn
import scala.collection.mutable.ListBuffer

def calculateHandValue(hand: List[Card]): Int = {
  var total = hand.map(_.rank.value).sum
  var aces = hand.count(_.rank == Rank.Ace)

  // While the total is over 21 and we have aces we can "demote" from 11 to 1
  while (total > 21 && aces > 0) {
    total -= 10 // Reduce value by 10 (from 11 to 1)
    aces -= 1
  }
  total
}

def interpretingHandValue(hand: List[Card]): String = {
  val value = calculateHandValue(hand)
  if (value > 21) "bust"
  else if (value == 21) "blackjack"
  else s"hand value is $value"
}

def finalComparison(name: String, playerValue: Int, dealerValue: Int): String = {
  if (dealerValue > 21 || playerValue > dealerValue) s"$name wins!"
  else if (playerValue < dealerValue) "Dealer wins!"
  else "It's a tie!"
}

// In your main game loop, you would call it like this:
// val (finalPlayerHand, deckAfterPlayer) = playerTurn(initialPlayerHand, deckAfterInitialDeal)
@main def play21(): Unit = {
  var input = ""
  println("Welcome to the game of 21! Give your name:")
  val name = StdIn.readLine().trim
  var cash = 1000 // Starting cash for the player

  while (input != "exit" && cash > 0) {
    println(s"--- Setting up a new game of 21 for $name with $$$cash ---")
    println("Type 'exit' to quit or enter amount to bet (default is 100):")
    var bet = 100 // Default bet amount
    input = StdIn.readLine().trim.toLowerCase
    if (input == "exit") {
      println("Thanks for playing! Goodbye!")
      return
    }else if (input.nonEmpty && input.forall(_.isDigit)) {
      bet = input.toInt
      if (bet > cash) {
        println(s"You can't bet more than you have! Current cash: $$$cash")
        input = ""
        bet = 100 // Reset to default bet
      }
    } else {
      println("Invalid input. Defaulting to a bet of $100.")
      bet = 100 // Reset to default bet
    }
    cash -= bet
    // 1. Create a new, shuffled deck of cards
    var currentDeck = Deck.standard52CardDeck.shuffle
    println(s"Shuffled a deck with ${currentDeck.cards.size} cards.")

    // 2. Deal two cards for a player's hand
    val (card1, deckAfter1) = currentDeck.deal.get
    val (card2, deckAfter2) = deckAfter1.deal.get
    currentDeck = deckAfter2

    var playerHand = ListBuffer(card1, card2)

    // Deal dealer's initial hand
    val (dealerCard1, deckAfterDealer1) = currentDeck.deal.get
    val (dealerCard2, deckAfterDealer2) = deckAfterDealer1.deal.get
    currentDeck = deckAfterDealer2

    val dealerHand = ListBuffer(dealerCard1, dealerCard2)

    println(s"$name's hand: ${playerHand.toList}")
    println(s"Dealer's front card: ${dealerCard1}, value: ${dealerCard1.rank.value}")

    // Player's turn
    var playerTurn = true
    while (playerTurn && calculateHandValue(playerHand.toList) < 21 && input != "exit") {
      println(interpretingHandValue(playerHand.toList))
      println("What do you want to do? (s - stand/ h - hit/exit)")
      input = StdIn.readLine().trim.toLowerCase

      if (input == "s") {
        playerTurn = false
      } else if (input == "h") {
        val (newCard, newDeck) = currentDeck.deal.get
        currentDeck = newDeck
        playerHand += newCard
        println(s"$name hits and gets: $newCard")
        println(s"$name's new hand: ${playerHand.toList}")

        if (calculateHandValue(playerHand.toList) > 21) {
          println(s"$name busted! You lose this round.")
          playerTurn = false
        }
      } else if (input == "exit") {
        println("Thanks for playing! Goodbye!")
        return
      } else {
        println("Invalid input. Please try again.")
      }
    }

    // Dealer's turn (only if player hasn't busted)
    if (calculateHandValue(playerHand.toList) <= 21) {
      println(s"Dealer's hand: ${dealerHand.toList}")

      while (calculateHandValue(dealerHand.toList) < 17) {
        val (dealerNewCard, newDeck) = currentDeck.deal.get
        currentDeck = newDeck
        dealerHand += dealerNewCard
        println(s"Dealer hits and gets: $dealerNewCard")
        println(s"Dealer's new hand: ${dealerHand.toList}")
      }

      // Final comparison
      val result = finalComparison(name, calculateHandValue(playerHand.toList),
        calculateHandValue(dealerHand.toList))
      println(result)

      // Update cash based on result
      if (result.contains(s"$name wins")) cash += bet * 2 // Player wins, gets back bet + winnings
      else if ("It's a tie!".equals(result)) cash += bet // Tie, gets back the bet
    }

    println(s"$name's cash: $cash")
    println("Press Enter to continue to the next game...")
    StdIn.readLine()
  }

  if (cash <= 0) {
    println("You're out of money! Game over.")
  }
}