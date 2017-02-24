package io.github.andres_vasquez.examplerecyclerdatagrid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridProperties;
import io.github.andres_vasquez.recyclerdatagrid.ui.fragments.DataGridFragmentFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;

    private Button mAddOneButton;
    private Button mAddMultipleButton;
    private EditText mSimulatorNumerEditText;

    private FrameLayout frameLayout;
    private int mActualCount=0;

    //Data Grid Fragment Object
    private DataGridFragmentFragment mDataGridFragment;

    //Columns for data displayed
    private List<ColumnItem> lstColumns;

    private static final String COLUMN_KEY_1="Column1";
    private static final String COLUMN_KEY_2="Column2";
    private static final String COLUMN_KEY_3="Column3";
    private static final String COLUMN_KEY_4="Column4";
    private static final String COLUMN_KEY_5="Column5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        //Init columns
        lstColumns=new ArrayList<>();

        //Adding click events
        mAddOneButton.setOnClickListener(this);
        mAddMultipleButton.setOnClickListener(this);

        mDataGridFragment =new DataGridFragmentFragment();

        //FillBase colunns
        lstColumns.add(new ColumnItem(1,"Column1"));
        lstColumns.add(new ColumnItem(2,"Column2"));
        lstColumns.add(new ColumnItem(3,"Column3"));
        lstColumns.add(new ColumnItem(4,"Column4"));
        lstColumns.add(new ColumnItem(5,"Column5"));

        //Arguments required columns
        DataGridProperties dataGridProperties=new DataGridProperties();
        dataGridProperties.setColumns(lstColumns);
        dataGridProperties.setUniqueColumn(COLUMN_KEY_1);

        //Set properties
        mDataGridFragment.setProperties(dataGridProperties);

        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(mDataGridFragment.getClass().getName())
                .add(R.id.frameLayout, mDataGridFragment, mDataGridFragment.getClass().getName())
                .commit();
    }

    /**
     * Init Visual components
     */
    private void initViews(){
        mAddOneButton=(Button)findViewById(R.id.add_one_button);
        mAddMultipleButton=(Button)findViewById(R.id.add_multiple_button);
        mSimulatorNumerEditText=(EditText)findViewById(R.id.simulator_number_editext);
        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_one_button:
                addOne();
                break;
            case R.id.add_multiple_button:

                break;
            default:
                break;
        }
    }

    private void addOne(){
        int newCount=mActualCount+1;
        Map<String,Object> mapRow=new LinkedHashMap<>();
        mapRow.put(COLUMN_KEY_1,"Cell 1."+newCount);
        mapRow.put(COLUMN_KEY_2,"Cell 2."+newCount);
        mapRow.put(COLUMN_KEY_3,"Cell 3."+newCount);
        mapRow.put(COLUMN_KEY_4,"Cell 4."+newCount);
        mapRow.put(COLUMN_KEY_5,"Cell 5."+newCount);
        mDataGridFragment.addRow(mapRow);
    }
}
