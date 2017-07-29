package ballidaku.videolistdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by brst-pc93 on 7/14/17.
 */

public class MyDialogs
{
    public Dialog dialog;


    private static MyDialogs  instance = new MyDialogs();

    public static MyDialogs getInstance()
    {
        return instance;
    }


    public EditText addDetail(final Context context, View.OnClickListener onClickListener)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_detail);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();


        EditText editTextInfo=(EditText) dialog.findViewById(R.id.editTextInfo);

        Button buttonAdd=(Button)dialog.findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(onClickListener);

        return editTextInfo;
    }




}
