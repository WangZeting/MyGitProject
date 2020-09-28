package org.example;

import java.util.Map;

public class InputExample {
    private int[] data;

    public InputExample(int number_one, int number_two, int number_three, int number_four, int number_five, int number_six, int number_seven) {
        int[] dataTemp = new int[7];
        dataTemp[0] = number_one;
        dataTemp[1] = number_two;
        dataTemp[2] = number_three;
        dataTemp[3] = number_four;
        dataTemp[4] = number_five;
        dataTemp[5] = number_six;
        dataTemp[6] = number_seven;
        this.data = dataTemp;
    }
    public void execute(){
        System.out.println(data[0]+data[1]+data[2]+data[3]+data[4]+data[5]+data[6]);
    }
}
