package io.github.andres_vasquez.recyclerdatagrid.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ChoiceItem;
import io.github.andres_vasquez.recyclerdatagrid.ui.adapters.CustomChoiceDialogAdapter;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;
import io.github.andres_vasquez.recyclerdatagrid.utils.GlobalFunctions;

/**
 * Created by avasquez on 6/9/2016.
 */

public class ListChoiceDialog extends DialogFragment implements AdapterView.OnItemClickListener{

    private Dialog dialog;

    private CardView cardSearch;
    private EditText txtSearch;
    private ListView lvItems;
    private CustomChoiceDialogAdapter adapter;
    private View viewChecked1;
    private View viewChecked2;
    private RelativeLayout lyNone;

    private static NoticeDialogListener mListener;
    private List<ChoiceItem> lstItems;

    private boolean isSearcheble=false;

    //Selection variables
    private List<ChoiceItem> lstItemsSelected=new ArrayList<ChoiceItem>();
    private static int choiceMode;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static ListChoiceDialog newInstance(int choiceMode, NoticeDialogListener mListener) {
        setmListener(mListener);
        setChoiceMode(choiceMode);

        ListChoiceDialog choiceDialog = new ListChoiceDialog();
        Bundle bundle = new Bundle();
        //choiceDialog.setArguments(bundle);
        return choiceDialog;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        lstItems=new ArrayList<ChoiceItem>();

        if(getArguments()!=null){
            Type listType = new TypeToken<ArrayList<ChoiceItem>>(){}.getType();
            lstItems= new Gson().fromJson(getArguments().getString(Constants.EXTRA_LIST_CHOICE_ITEMS), listType);

            isSearcheble=getArguments().getBoolean(Constants.TAG_SEARCHABLE,false);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_list, null);

        lvItems =(ListView)rootView.findViewById(R.id.lvItems);
        cardSearch=(CardView)rootView.findViewById(R.id.cardSearch);
        txtSearch=(EditText)rootView.findViewById(R.id.txtSearch);
        txtSearch.getBackground().mutate().setColorFilter(getResources().getColor(R.color.dividers), PorterDuff.Mode.SRC_ATOP);
        lvItems.requestFocus();
        viewChecked1=(View)rootView.findViewById(R.id.viewChecked1);
        viewChecked2=(View)rootView.findViewById(R.id.viewChecked1);
        lyNone=(RelativeLayout)rootView.findViewById(R.id.lyNone);

        viewChecked1.setVisibility(View.GONE);
        viewChecked2.setVisibility(View.GONE);
        lyNone.setVisibility(View.GONE);

        adapter=new CustomChoiceDialogAdapter(getActivity(),lstItems);
        lvItems.setAdapter(adapter);

        if(isSearcheble){
            cardSearch.setVisibility(View.VISIBLE);
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s.toString());
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        else{
            cardSearch.setVisibility(View.GONE);
        }

        lvItems.setOnItemClickListener(this);
        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        builder.setView(rootView);
        /* Add action buttons */
        /*        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(lstItemsSelected);
                        //dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick();
                    }
        });*/
        dialog=builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                txtSearch.clearFocus();
                GlobalFunctions.hideKeyboard(txtSearch,getActivity());
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    public static int getChoiceMode() {
        return choiceMode;
    }

    public static void setChoiceMode(int choiceMode) {
        ListChoiceDialog.choiceMode = choiceMode;
    }

    public NoticeDialogListener getmListener() {
        return mListener;
    }

    public static void setmListener(NoticeDialogListener mListener) {
        ListChoiceDialog.mListener = mListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Constants.LIST_DIALOG_SINGLE_CHOICE==choiceMode){
            ChoiceItem itemSelected=(ChoiceItem) parent.getItemAtPosition(position);
            for(ChoiceItem item : lstItems){
                if(item.getId()==itemSelected.getId()){
                    item.setChecked(true);
                }
                else{
                    item.setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();

            if(dialog.isShowing())
                dialog.dismiss();
            mListener.onDialogItemClick(itemSelected);
        }
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(List<ChoiceItem> lstItemsSelected);
        public void onDialogNegativeClick();
        public void onDialogItemClick(ChoiceItem itemSelected);
    }
}
