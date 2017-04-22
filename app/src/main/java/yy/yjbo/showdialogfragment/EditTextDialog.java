package yy.yjbo.showdialogfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import yy.yjbo.yutil.BaseBottomDialog;
import yy.yjbo.R;

/**
 * Created by shaohui on 16/10/12.
 */

public class EditTextDialog extends BaseBottomDialog {

    private EditText mEditText;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_edit_text;
    }

    @Override
    public void bindView(View v) {
        mEditText = (EditText) v.findViewById(R.id.edit_text);
//        mEditText.post(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager imm =
//                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(mEditText, 0);
//            }
//        });
        //监听实体键的返回键  http://www.cnblogs.com/yejiurui/p/3757493.html
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                if (keyCode == KeyEvent.KEYCODE_SEARCH)
                    return true; // pretend we've processed it
                else {
                    System.out.println("==00==4=");
                    getDialog().setCancelable(true);
                    return false; // 传个上个onkeydown方法处理
//                    return super.onKeyDown(keyCode, event);
                }
            }
        });

    }


    @Override
    public float getDimAmount() {
        return 0.9f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
