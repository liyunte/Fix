package com.lyt.fix;

import android.content.Context;
import android.widget.Toast;

public class Test {
    public Test(){

    }
    public void div(Context context){
        int a =10;
        int b = 0;
        int c = a/b;
        Toast.makeText(context,"c >>> "+c,Toast.LENGTH_SHORT).show();
    }
}
