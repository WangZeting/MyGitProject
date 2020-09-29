package org.example;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CardCreator cardCreator = new CardCreator();
        CardDistributor cardDistributor = new CardDistributor(cardCreator.createCards());
        for (Map.Entry<Integer,Card> entry : cardDistributor.getDistributor_alpha().entrySet()){
            System.out.println(entry.getValue().getCardName());
        }
    }
}
