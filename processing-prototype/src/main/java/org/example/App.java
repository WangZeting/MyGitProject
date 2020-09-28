package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CardsCreator creator = new CardsCreator();
        for(Card card : creator.createCards()){
            System.out.println(card.getCardName()+card.getCardColor()+card.getCardRank());
        }
    }
}
