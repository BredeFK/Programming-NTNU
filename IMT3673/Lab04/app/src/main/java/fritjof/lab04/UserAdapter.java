// Author: Brede Fritjof Klausen

package fritjof.lab04;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private int id;

    public UserAdapter(Context context, int id){
        super(context, id);

        this.activity = (Activity)context;
        this.id = id;
    }

    @Override
    @NonNull
    public View getView(int pos, View convertView, @NonNull ViewGroup container){
        if(convertView == null){
            convertView = this.activity.getLayoutInflater().inflate(this.id, container, false);
        }
        String row = this.getItem(pos);
        TextView user = convertView.findViewById(R.id.usersID);

        if ((row != null) && (user != null)){
            user.setText(row);
        }


        return convertView;
    }
}
