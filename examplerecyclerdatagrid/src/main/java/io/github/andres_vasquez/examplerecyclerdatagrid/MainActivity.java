package io.github.andres_vasquez.examplerecyclerdatagrid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.CellProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridProperties;
import io.github.andres_vasquez.recyclerdatagrid.ui.fragments.DataGridFragmentFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;

    private ImageView mFilterRowsImageView;
    private ImageView mFilterColumnsImageView;
    private EditText mSearchEditText;

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
        //Filters
        mFilterRowsImageView.setOnClickListener(this);
        mFilterColumnsImageView.setOnClickListener(this);

        //Bottom bar
        mAddOneButton.setOnClickListener(this);
        mAddMultipleButton.setOnClickListener(this);


        mDataGridFragment =new DataGridFragmentFragment();

        //FillBase colunns
        ColumnItem columnItem1=new ColumnItem(1,"Column1");
        columnItem1.setCellProperties(new CellProperties(200, Color.RED, 10, Gravity.RIGHT));

        lstColumns.add(columnItem1);
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
        //Top bar
        mFilterRowsImageView=(ImageView)findViewById(R.id.filter_rows_imageview);
        mFilterColumnsImageView=(ImageView)findViewById(R.id.filter_columns_imageview);
        mSearchEditText=(EditText)findViewById(R.id.search_edittext);

        //Datagrid fragment
        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);

        //Bottom Bar
        mAddOneButton=(Button)findViewById(R.id.add_one_button);
        mAddMultipleButton=(Button)findViewById(R.id.add_multiple_button);
        mSimulatorNumerEditText=(EditText)findViewById(R.id.simulator_number_editext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgFilterRows:

                break;
            case R.id.imgFilterColumns:
                mDataGridFragment.showColumnsPickUpDialog();
                break;
            case R.id.add_one_button:
                addOne();
                break;
            case R.id.add_multiple_button:

                break;
            default:
                break;
        }
    }

    /**
     * Add one Cell
     */
    private void addOne(){
        Random r = new Random();
        int Low = 0;
        int High = 1000;
        int random = 0;

        Map<String,Object> mapRow=new LinkedHashMap<>();
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_1,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_2,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_3,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_4,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_5,"Data "+random);
        mDataGridFragment.addRow(mapRow);
    }
}
