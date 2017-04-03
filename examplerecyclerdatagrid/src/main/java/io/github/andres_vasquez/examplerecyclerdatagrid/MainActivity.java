package io.github.andres_vasquez.examplerecyclerdatagrid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ChoiceItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.RowSelectorStyle;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.FilterColumnInterface;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.FilterRowInterface;
import io.github.andres_vasquez.recyclerdatagrid.ui.adapters.HorizontalAdapter;
import io.github.andres_vasquez.recyclerdatagrid.ui.fragments.DataGridFragment;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, HorizontalAdapter.OnItemClickListener {

    private Context mContext;

    private ImageView mFilterRowsImageView;
    private ImageView mFilterColumnsImageView;
    private EditText mSearchEditText;

    private Button mAddOneButton;
    private Button mAddMultipleButton;
    private Button mClearButton;
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

    private static final String COLUMN_KEY_1="Image";
    private static final String COLUMN_KEY_2="Column2";
    private static final String COLUMN_KEY_3="Column3";
    private static final String COLUMN_KEY_4="Column4";
    private static final String COLUMN_KEY_5="Column5";


    private static final int STATE_1=1;
    private static final int STATE_2=2;
    private static final int STATE_3=3;
    private static final int STATE_4=4;
    private static final int STATE_5=5;

    private boolean selectable=false;
    private List<ChoiceItem> lstFilters;

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

        mClearButton.setOnClickListener(this);
        mSelectableCheckBox.setOnCheckedChangeListener(this);


        mDataGridFragment =new DataGridFragment();

        //Fill Base columns
        ColumnItem columnItem1=new ColumnItem(1,COLUMN_KEY_1, Constants.COLUMN_TYPE_IMAGE);
        columnItem1.setFixed(true);
        CellProperties cellProperties=new CellProperties(100,
                Color.BLACK,
                16,
                Gravity.CENTER);
        columnItem1.setCellProperties(cellProperties);

        lstColumns.add(columnItem1);
        lstColumns.add(new ColumnItem(2,COLUMN_KEY_2, Constants.COLUMN_TYPE_DEFAULT));
        lstColumns.add(new ColumnItem(3,COLUMN_KEY_3, Constants.COLUMN_TYPE_DEFAULT));
        lstColumns.add(new ColumnItem(4,COLUMN_KEY_4, Constants.COLUMN_TYPE_DEFAULT));
        lstColumns.add(new ColumnItem(5,COLUMN_KEY_5, Constants.COLUMN_TYPE_DEFAULT));

        //Create filters
        lstFilters=new ArrayList<>();
        lstFilters.add(new ChoiceItem(1,"All",0,COLUMN_KEY_1,null,true));
        lstFilters.add(new ChoiceItem(2,"State1",R.drawable.icon_ok,String.valueOf(STATE_1)));
        lstFilters.add(new ChoiceItem(3,"State2",R.drawable.icon_ok_partial,String.valueOf(STATE_2)));
        lstFilters.add(new ChoiceItem(4,"State3",R.drawable.icon_plus,String.valueOf(STATE_3)));
        lstFilters.add(new ChoiceItem(5,"State4",R.drawable.icon_wrong, String.valueOf(STATE_4)));


        //Arguments required columns
        dataGridProperties=new DataGridProperties();
        dataGridProperties.setColumns(lstColumns);
        dataGridProperties.setUniqueColumn(COLUMN_KEY_2);

        //Set properties
        mDataGridFragment.setProperties(dataGridProperties);
        mDataGridFragment.addFilters(lstFilters);
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

        mClearButton = (Button)findViewById(R.id.clear_button);
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

        int state=0;

        DataGridItem dataGridItem=new DataGridItem();
        Map<String,Object> mapRow=new LinkedHashMap<>();
        random=r.nextInt(High-Low) + Low;
        if(0<random && random<200){
            mapRow.put(COLUMN_KEY_1,R.drawable.icon_ok);
            state=STATE_1;
        } else if(201<random && random<400){
            mapRow.put(COLUMN_KEY_1,R.drawable.icon_ok_partial);
            state=STATE_2;
        } else if(401<random && random<600){
            mapRow.put(COLUMN_KEY_1,R.drawable.icon_plus);
            state=STATE_3;
        } else if(601<random){
            mapRow.put(COLUMN_KEY_1,R.drawable.icon_wrong);
            state=STATE_4;
        }

        //random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_2,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_3,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_4,"Data "+random);
        random=r.nextInt(High-Low) + Low;
        mapRow.put(COLUMN_KEY_5,"Data "+random);

        dataGridItem.setMapData(mapRow);
        dataGridItem.setFilterValue(String.valueOf(state));
        dataGridItem.setUniqueIdentificator(mapRow.get(COLUMN_KEY_2).toString());
        mDataGridFragment.addOrUpdate(dataGridItem);
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
            case R.id.clear_button:
                mDataGridFragment.clear();
                break;

            case R.id.filter_rows_imageview:
                mDataGridFragment.showRowPickerDialog(new FilterRowInterface() {
                    @Override
                    public void onRowFilterApplied(ChoiceItem filterItem) {
                        Log.e("Filter",filterItem.getText());
                        mDataGridFragment.applyFilter(filterItem.getFilterValue());
                    }
                });
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

            globalTagItem.getMapData().put(COLUMN_KEY_3,"Updated");
            mDataGridFragment.addOrUpdate(globalTagItem);
        }
    }
}
