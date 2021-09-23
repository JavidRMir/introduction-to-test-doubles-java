package testhelper;

import java.util.Random;

public class RandomStub extends Random {

    private int value;

    public RandomStub(int value) {
        this.value = value;
    }

    //has same method signature as Random.nextInt to provide a RandomStub.nextInt implementation
    @Override
    public int nextInt(int value) {
        return this.value;
    }


}
