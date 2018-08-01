package aurora.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

import static android.R.attr.id;
import static java.lang.Boolean.FALSE;
import static net.aurora.main.R.id.profileIcon;

/**
 * Item Adapter - creates a bridge between the recycler view items and an arrayList of Profiles or Documents.
 */


public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public final int VIEW_DOCUMENT = 0;
    public final int VIEW_PROFILE = 1;


    private LayoutInflater inflater;
    private ArrayList<Profile> users;
    private ArrayList<Document> documents;

    private Context theContext;
    private BaseActivity theActivity;
    private RecyclerView.ViewHolder selectedHolder;

    public ItemAdapter(Context context, ArrayList<Profile> users, ArrayList<Document> documents){
       inflater = LayoutInflater.from(context);
        this.users = users;
        this.documents = documents;
        theContext = context;
        theActivity = (BaseActivity)context;
    }


    //onCreateViewHolder - determines which view holder to make for each item.

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_item_layout, parent, false);

        if(viewType == VIEW_PROFILE){
            return new ProfileViewHolder(view);
        }

        if(viewType == VIEW_DOCUMENT){
            return new DocumentViewHolder(view);
        }

        return null;
    }


    //getItemCount - get the total number of items in the list.

    @Override
    public int getItemCount(){
        return getUserCount() + getDocumentCount();
    }

    //getUserCount - get the total number of users in the list.

   private int getUserCount(){
       int userCount = 0;
       if(users != null){
           userCount = users.size();
       }
       return userCount;
   }

    //getDocumentCount - get the total number of documents in the list.

    private int getDocumentCount(){
        int documentCount = 0;
        if(documents != null){
            documentCount = documents.size();
        }
        return documentCount;
    }


    //getItemViewType - Dictates which view type the list object is.

    @Override
    public int getItemViewType(int position){
        if(position < getUserCount()){
            return VIEW_PROFILE;
        }

        if(position - getUserCount() < getDocumentCount()){
            return VIEW_DOCUMENT;
        }

        return -1;
    }


    //onBindViewHolder - Sets up each view holder when they are paired with a list item.

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProfileViewHolder){
            Profile user = users.get(position);
            ProfileViewHolder profileHolder = (ProfileViewHolder)holder;
            profileHolder.lblUsername.setText(user.getUsername());
            profileHolder.lblName.setText(user.getFirstName() + " " + user.getLastName());

            Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            profileHolder.profileIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
            profileHolder.setUser(user);

            if(holder.equals(selectedHolder) &&  profileHolder.getUser().isSelected()){
                profileHolder.btnView.setVisibility(profileHolder.btnView.VISIBLE);
                profileHolder.item.setBackgroundColor(0xFFCEC5D8);

            } else {
                profileHolder.btnView.setVisibility( profileHolder.btnView.INVISIBLE);
                profileHolder.item.setBackgroundColor(0xFFFAFAFA);
            }

        }

        if(holder instanceof DocumentViewHolder){

            DocumentViewHolder documentHolder = (DocumentViewHolder)holder;

            Document document = documents.get(position);
            documentHolder.lblUsername.setText(document.getTitle());
            //documentHolder.lblName.setText(document.getAuthor().toString());
            Bitmap bmp = BitmapFactory.decodeByteArray(document.getImage(), 0, document.getImage().length);
            documentHolder.profileIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
            documentHolder.lblName.setText("by " + document.getAuthor().getUsername());

            /**
            if(document.getText() != null) {
                int length = document.getText().length();
                if (length >= 20) {
                    documentHolder.lblName.setText(document.getText().substring(0, 20) + "...");
                }
            }
             **/

            documentHolder.setDocument(document);

            if(documentHolder.equals(selectedHolder)){
                documentHolder.btnView.setVisibility(documentHolder.btnView.VISIBLE);
                documentHolder.item.setBackgroundColor(0xFFCEC5D8);

            } else {
                documentHolder.btnView.setVisibility(documentHolder.btnView.INVISIBLE);
                documentHolder.item.setBackgroundColor(0xFFFAFAFA);
            }

        }

    }


    //getSelectedId - returns the id of the selected object.

    public int getSelectedId(int viewType){
        if(viewType == VIEW_PROFILE) {
            ProfileViewHolder holder = (ProfileViewHolder)selectedHolder;
            return holder.getUser().getUserID();
        }

        if(viewType == VIEW_DOCUMENT){
            DocumentViewHolder holder = (DocumentViewHolder)selectedHolder;
            return holder.getDocument().getID();
        }

        return -1;
    }

    //ProfileViewHolder - defines how the profile object is converted into the view holder.

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileIcon;
        private TextView lblUsername;
        private TextView lblName;
        private RelativeLayout item;
        private Button btnView;
        private Profile theUser;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            profileIcon = (ImageView)itemView.findViewById(R.id.iconView);
            lblUsername = (TextView)itemView.findViewById(R.id.lblPassword);
            lblName = (TextView)itemView.findViewById(R.id.lblFirstName);
            item = (RelativeLayout)itemView.findViewById(R.id.item);
            btnView = (Button)itemView.findViewById(R.id.btnView);

            setupListeners();
        }


        //setupListeners - adds the listeners to all of the necessary layout elements.

        private void setupListeners(){

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request req = new Request(6);
                    req.setSwitch(true);
                    req.setValue(theUser.getUserID());
                    Response res = theActivity.serverRequest(req);
                    if(res.getSwitch()) {
                        theActivity.startActivity(new Intent(theContext, HomeActivity.class));
                    } else {
                        theActivity.startActivity(new Intent(theContext, ProfileActivity.class));
                    }
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileViewHolder holder = (ProfileViewHolder)selectedHolder;
                    if(selectedHolder != null) {
                        holder.getUser().setSelected(false);
                        holder.item.setBackgroundColor(0xFFFAFAFA);
                        holder.btnView.setVisibility(btnView.INVISIBLE);
                    }

                    if(!ProfileViewHolder.this.equals(selectedHolder)){
                        item.setBackgroundColor(0xFFCEC5D8);
                        btnView.setVisibility(btnView.VISIBLE);
                        ProfileViewHolder.this.getUser().setSelected(true);
                        selectedHolder = ProfileViewHolder.this;
                    } else {
                        selectedHolder = null;
                    }

                }
            });


        }

        //getUser - returns the profile stored in the view holder.

        public Profile getUser() {
            return theUser;
        }

        //setUser - sets the profile stored in the view holder.
        public void setUser(Profile user) {
            this.theUser = user;
        }
    }


    //ProfileViewHolder - defines how the document object is converted into the view holder.

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


        //setupListeners - adds the listeners to all of the necessary layout elements.

        private void setupListeners(){

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request req = new Request(6);
                    req.setValue(theDocument.getID());
                    req.setSwitch(false);
                    theActivity.serverRequest(req);
                    theActivity.startActivity(new Intent(theContext, DocumentActivity.class));
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedHolder != null) {
                        DocumentViewHolder holder = (DocumentViewHolder)selectedHolder;
                        holder.item.setBackgroundColor(0xFFFAFAFA);
                        holder.btnView.setVisibility(btnView.INVISIBLE);
                    }

                    if(!ItemAdapter.DocumentViewHolder.this.equals(selectedHolder)){
                        item.setBackgroundColor(0xFFCEC5D8);
                        btnView.setVisibility(btnView.VISIBLE);
                        selectedHolder = ItemAdapter.DocumentViewHolder.this;
                    } else {
                        selectedHolder = null;
                    }
                }
            });


        }

        //getDocument - returns the document stored in the view holder.

        public Document getDocument() {
            return theDocument;
        }

        //setDocument - sets the document stored in the view holder.

        public void setDocument(Document doc) {
            this.theDocument = doc;
        }
    }


}
