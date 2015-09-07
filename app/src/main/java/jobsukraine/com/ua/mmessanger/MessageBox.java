package jobsukraine.com.ua.mmessanger;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.Date;

public class MessageBox extends Activity {

    public static final String HOST = "192.168.1.35";
    public static final int PORT = 5222;

    ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT);
    XMPPConnection connection = new XMPPConnection(connConfig);
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textView = ((TextView) findViewById(R.id.message));

        ChatManager chatmanager = connection.getChatManager();
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            public void chatCreated(final Chat chat, final boolean createdLocally) {
                chat.addMessageListener(new MessageListener() {
                    public void processMessage(Chat chat, final Message message) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (message.getBody()!=null) {
                                    textView.setText(message.getFrom() +":\n"+ message.getBody() +"\n"+ new Date());
                                }
                            }
                        });
                    }
                });
            }
        });

        connectAndLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_box, menu);
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

    public void connectAndLogin() {

        connection.disconnect();

        try {
            connection.connect();
            connection.login(getIntent().getStringExtra("username"), getIntent().getStringExtra("password"));
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }
}
