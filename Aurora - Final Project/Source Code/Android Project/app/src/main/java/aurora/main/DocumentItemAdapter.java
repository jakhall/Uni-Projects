package aurora.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import net.aurora.main.R;

import java.util.ArrayList;

import aurora.model.Document;
import aurora.network.Request;


public class DocumentItemAdapter extends RecyclerView.Adapter<DocumentItemAdapter.DocumentViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Document> documents = new ArrayList<Document>();
    private Context theContext;
    private BaseActivity theActivity;
    private DocumentViewHolder selectedHolder;

    public DocumentItemAdapter(Context context, ArrayList<Document> documents){
        inflater = LayoutInflater.from(context);
        this.documents = documents;
        theContext = context;
        theActivity = (BaseActivity)context;

    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_item_layout, parent, false);
        DocumentViewHolder holder = new DocumentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        Document document = documents.get(position);
        holder.lblUsername.setText(document.getTitle());
        int length = document.getText().length();
        if(length >= 20) {
            holder.lblName.setText(document.getText().substring(0, 20) + "...");
        }


        //int id = theActivity.getResources().getIdentifier("default_" + user.getDefaultColor(), "drawable", theActivity.getPackageName());

       // holder.profileIcon.setImageResource(id);

       holder.setDocument(document);

        if(holder.equals(selectedHolder)){
            holder.btnView.setVisibility(holder.btnView.VISIBLE);
            holder.item.setBackgroundColor(0xFFCEC5D8);

        } else {
            holder.btnView.setVisibility(holder.btnView.INVISIBLE);
            holder.item.setBackgroundColor(0xFFFAFAFA);
        }

    }

    @Override
    public void onViewRecycled(DocumentViewHolder holder) {
        super.onViewRecycled(holder);

    }

    public int getSelectedId(){
        return selectedHolder.getDocument().getID();
    }


    @Override
    public int getItemCount() {
        return documents.size();
    }


    public class DocumentViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileIcon;
        private TextView lblUsername;
        private TextView lblName;
        private RelativeLayout item;
        private Button btnView;
        private Document theDocument;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            profileIcon = (ImageView)itemView.findViewById(R.id.iconView);
            lblUsername = (TextView)itemView.findViewById(R.id.lblPassword);
            lblName = (TextView)itemView.findViewById(R.id.lblFirstName);
            item = (RelativeLayout)itemView.findViewById(R.id.item);
            btnView = (Button)itemView.findViewById(R.id.btnView);

            setupListeners();
        }


        private void setupListeners(){

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request req = new Request(6);
                    req.setValue(theDocument.getID());
                    theActivity.serverRequest(req);
                    theActivity.startActivity(new Intent(theContext, DocumentActivity.class));
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedHolder != null) {
                       // selectedHolder.getDocument().setSelected(false);
                        selectedHolder.item.setBackgroundColor(0xFFFAFAFA);
                        selectedHolder.btnView.setVisibility(btnView.INVISIBLE);
                    }

                    if(!DocumentViewHolder.this.equals(selectedHolder)){
                        item.setBackgroundColor(0xFFCEC5D8);
                        btnView.setVisibility(btnView.VISIBLE);
                       // DocumentViewHolder.this.getDocument().setSelected(true);
                        selectedHolder = DocumentViewHolder.this;
                    } else {
                        selectedHolder = null;
                    }

                }
            });


        }

        public Document getDocument() {
            return theDocument;
        }

        public void setDocument(Document doc) {
            this.theDocument = doc;
        }
    }

}
