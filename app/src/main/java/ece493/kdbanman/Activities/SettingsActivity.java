package ece493.kdbanman.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ece493.kdbanman.Model.ImageFilter;
import ece493.kdbanman.Model.ModelServer;
import ece493.kdbanman.ObserverActivity;
import ece493.kdbanman.R;

public class SettingsActivity extends ObserverActivity {

    TextView kernelSizeTextView;
    EditText kernelSizeEditText;

    ImageFilter imageFilter;

    Toast badKernelToast;

    @Override
    protected void renderViews() {
        if (kernelSizeTextView == null) {
            Log.w("SettingsActivity", "renderViews() called before views initialized.");
            return;
        }

        kernelSizeTextView.setText(Integer.toString(imageFilter.getKernelSize()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageFilter = ModelServer.getInstance().getImageFilter(this);

        badKernelToast = Toast.makeText(this, R.string.kernel_size_error, Toast.LENGTH_SHORT);

        kernelSizeTextView = (TextView) findViewById(R.id.textViewReadOnlyKernelSize);
        kernelSizeEditText = (EditText) findViewById(R.id.editTextKernelSize);

        kernelSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int kernelSize = Integer.parseInt(s.toString());
                    imageFilter.setKernelSize(kernelSize);
                    badKernelToast.cancel();
                } catch (NumberFormatException e) {
                    badKernelToast.show();
                    Log.w("SettingsActivity", "Kernel size input was not an integer.");
                } catch (IllegalArgumentException e) {
                    badKernelToast.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
