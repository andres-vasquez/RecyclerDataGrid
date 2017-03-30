# RecyclerDataGrid

Show complex data tables in your App.
- Dynamic add/remove/update rows trought ReciclerViews
- Dynamic Columns.
- Dynamic filters.
- Order by ASC & DESC

Download
--------

```groovy
dependencies {
    compile 'RecyclerDataGrid:recyclerdatagrid:0.1.1'
}
```

Step 1: Define column keys
--------

```java
private static final String COLUMN_KEY_1="Column1";
private static final String COLUMN_KEY_2="Column2";
private static final String COLUMN_KEY_3="Column3";
private static final String COLUMN_KEY_4="Column4";
private static final String COLUMN_KEY_5="Column5";
```

Step 2: Create columns and put into List<ColumnItem>
--------

Simple mode: id and Key only
```java
List<ColumnItem> lstColumns=new ArrayList<>();
    lstColumns.add(new ColumnItem(2,COLUMN_KEY_2));
    lstColumns.add(new ColumnItem(3,COLUMN_KEY_3));
    lstColumns.add(new ColumnItem(4,COLUMN_KEY_4));
```
Complex mode: add cell properties. Ex. Width, Color, textSize and Gravity
```java
ColumnItem columnItem1=new ColumnItem(1,COLUMN_KEY_1);
columnItem1.setCellProperties(new CellProperties(200, Color.RED, 10, Gravity.RIGHT));
lstColumns.add(columnItem1);
```

Step 3: Create DataGrid object
--------

```java
//Arguments required columns
dataGridProperties=new DataGridProperties();
dataGridProperties.setColumns(lstColumns);
dataGridProperties.setUniqueColumn(COLUMN_KEY_1);

//Set properties
mDataGridFragment.setProperties(dataGridProperties);
mDataGridFragment.addItemClickListener(this);
```

Step 4: Attach DataGrid as a Fragment
--------
```java
FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
            .addToBackStack(mDataGridFragment.getClass().getName())
            .add(R.id.frameLayout, mDataGridFragment, mDataGridFragment.getClass().getName())
            .commit();
```

Step 5: Add data and enojoy.
--------
```java
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
```

Customization
----
Clone repo and run the sample App for more functions and customization.

Contribution
----
Please feel free to create issues in my repo, fork the repo and make pull requests.

License
----

Apache 2.0
