package io.github.andres_vasquez.examplerecyclerdatagrid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.CellProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.RowSelectorStyle;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.FilterColumnInterface;
import io.github.andres_vasquez.recyclerdatagrid.ui.adapters.HorizontalAdapter;
import io.github.andres_vasquez.recyclerdatagrid.ui.fragments.DataGridFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, HorizontalAdapter.OnItemClickListener {

    private Context mContext;

    private ImageView mFilterRowsImageView;
    private ImageView mFilterColumnsImageView;
    private EditText mSearchEditText;

    private Button mAddOneButton;
    private Button mAddMultipleButton;
    private EditText mSimulatorNumerEditText;

    private CheckBox mSelectableCheckBox;
    private TextView mResultTextView;

    private FrameLayout frameLayout;
    private int mActualCount=0;

    //Data Grid Fragment Object
    private DataGridFragment mDataGridFragment;
    private DataGridProperties dataGridProperties;

    //Columns for data displayed
    private List<ColumnItem> lstColumns;

    private static final String COLUMN_KEY_1="Column1";
    private static final String COLUMN_KEY_2="Column2";
    private static final String COLUMN_KEY_3="Column3";
    private static final String COLUMN_KEY_4="Column4";
    private static final String COLUMN_KEY_5="Column5";

    private boolean selectable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

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

        mSelectableCheckBox.setOnCheckedChangeListener(this);


        mDataGridFragment =new DataGridFragment();

        //FillBase colunns
        ColumnItem columnItem1=new ColumnItem(1,"Column1");
        columnItem1.setCellProperties(new CellProperties(200, Color.RED, 10, Gravity.RIGHT));

        lstColumns.add(columnItem1);
        lstColumns.add(new ColumnItem(2,"Column2"));
        lstColumns.add(new ColumnItem(3,"Column3"));
        lstColumns.add(new ColumnItem(4,"Column4"));
        lstColumns.add(new ColumnItem(5,"Column5"));

        //Arguments required columns
        dataGridProperties=new DataGridProperties();
        dataGridProperties.setColumns(lstColumns);
        dataGridProperties.setUniqueColumn(COLUMN_KEY_1);

        //Set properties
        mDataGridFragment.setProperties(dataGridProperties);
        mDataGridFragment.addItemClickListener(this);

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

        mSelectableCheckBox=(CheckBox)findViewById(R.id.selectable_checkbox);
        mResultTextView =(TextView)findViewById(R.id.result_textview);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if(compoundButton.getId()==R.id.selectable_checkbox){
            selectable=checked;
            dataGridProperties.setSelectable(checked);

            if(selectable){
                //Custom selector
                RowSelectorStyle rowSelectorStyle = new RowSelectorStyle();
                rowSelectorStyle.setBackgroundColor(mContext.getResources().getColor(R.color.bkg_info));
                rowSelectorStyle.setBackgroundColorSelected(mContext.getResources().getColor(R.color.bkg_success));
                rowSelectorStyle.setImageSelectorSelected(R.drawable.icon_check_blue);
                rowSelectorStyle.setImageSelector(R.drawable.icon_wrong);
                dataGridProperties.setCustomSelector(rowSelectorStyle);
            }
            mDataGridFragment.applyProperties(dataGridProperties);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.filter_rows_imageview:
                break;
            case R.id.filter_columns_imageview:
                mDataGridFragment.showColumnsPickUpDialog(new FilterColumnInterface() {
                    @Override
                    public void onColumnsSelectedChanged(List<ColumnItem> lstColumns) {
                        //Some validations. Ex. Min 2 columns
                        int selectedCount=0;
                        for (ColumnItem column : lstColumns){
                            if(column.isSelected()){
                                selectedCount++;
                            }
                        }

                        if(selectedCount<2){
                            Toast.makeText(mContext,"You have to select at least 2 columns",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mDataGridFragment.applyColumnChanges(lstColumns);
                        }
                    }
                });
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

    @Override
    public void onItemClick(DataGridItem globalTagItem, int position) {
        if(selectable){
            //Show number of selected items
            mResultTextView.setText("Selected: "+mDataGridFragment.getSelectedItems().size());
        } else {
            //Show unique label clicked
            Map<String,Object> mapData=globalTagItem.getMapData();
            mResultTextView.setText("Unique value: "+mapData.get(globalTagItem.getUniqueIdentificator().toString())
                    .toString());
        }
    }
}
