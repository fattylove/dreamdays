package al.com.dreamdays.activity;

import al.com.dreamdays.base.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.guxiu.dreamdays.R;

public class EditTextActivity extends BaseActivity {
	
	private EditText editorView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_dreamdays_edittext_layout);
		editorView = (EditText)this.findViewById(R.id.editorView);
		
		editorView.setImeOptions(EditorInfo.IME_ACTION_DONE);
		editorView.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				Intent intent = new Intent();
				intent.putExtra("editor", editorView.getText().toString().trim());
				setResult(RESULT_OK, intent);
				
				editorView.setText("");
				finish();
				return false;
			}
		});
	}

}
