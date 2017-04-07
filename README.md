# ListAPI

An API for the easy usage of a configureable ListView with the features of a **sort mechanism** and adding fast **custom buttons with slide function**.
It also supports multiple list items!

![](https://cdn.pbrd.co/images/1kdzBq37m.png)


## Usage:

1. Define your foreground layout and the item (ViewItem and Holder) itself

##### Just a normal XML layout

        <TextView android:id="@+id/txt_dynamic2" xmlns:android="http://schemas.android.com/apk/res/android"
        android:textAppearance="?android:attr/textAppearanceMedium"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:minWidth="60dp"
        android:maxWidth="300dp"
        ...
  -------------   
  
##### When defining the holder (containing the neccessary elements) extend from CBViewHolder 

         public class MyHolder extends CBViewHolder {
            public TextView name;
        }
  -------------   
  
#####  When defining the item extend from CBListViewItem
  
        public class MyListViewItem extends CBListViewItem<MyHolder, String> {


        public MyListViewItem(String item, MyHolder holder, int itemResource) {
            super(item, holder, itemResource); 
    
###### Adding built in delete and edit button (also one custom)

            this.addDelete = true;
            this.addEdit = true;
            this.customButtons.add(new MyButton(220, R.color.yellow, -1));
        }

 
###### Setup the functionality

        public MyHolder setUpView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final IListMenuListener listMenuListener, final int highlightPos, final IOneClickListener oneClickListener, final LayoutInflater inflater, final SwipeListener swipeListener, Context context) {

            MyHolder test = (MyHolder)holder;
            test.name.setText(item);
            test.name.setEnabled(true);
            test.name.setTextColor(Color.GREEN);
            if (isSortMode) {
                test.name.setOnClickListener(null);
            } else {
                test.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listMenuListener.handleShow(item);
                    }
                });
            }
            return test;
        }

###### Init the the layout to the holder

        public MyHolder initView(View itemView, final Context context) {
            MyHolder test = (MyHolder)holder;
            test.name = (TextView) itemView.findViewById(R.id.txt_dynamic2);

            return test;
        }
        
2. Define the Adapter which connects the list and extend from CBAdapter
        
        public class MyAdapter extends CBAdapter<MyListViewItem> {
            public MyAdapter(Context context, List<MyListViewItem> data) {
                super(context, data);
            }

            @Override
            public void handleSingleClick(final int position) {
                final String item = (String) this.data.get(position).getItem();
                Toast.makeText(context, "got to next view", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleLongClick(int position) {

            }
        }

3. Define the Activity which uses the list and extend from CBActivity

        public class MainActivity extends CBListActivity<ViewItem, MyAdapter> {
            private static final int MENU_ITEM_ITEM1 = 1;
            List<ViewItem> test;

            @Override
            public void onResume() {
                super.onResume();
                updateData();
                updateAdapter();
            }

            public MyAdapter initAdapter() {
                this.test = new ArrayList<>();
                for (int i = 0; i < 20; i++) {

                    String item = "item 11111111111" + i;
                    
###### Add list elements (needs the data, the holder and the layout)

                    MyListViewItem li = new MyListViewItem(item, new MyHolder(), R.layout.foreground_standard);
                    test.add(li);
                }
                return new MyAdapter(this, test);
            }

            @Override
            public void updateData() {

            }

            public List<ViewItem> getUpdatedData() {
                return this.test;
            }

###### Standard calls for built in buttons delete and edit  (must not be used)

            @Override
            public void handleDelete(Object o) {
                Toast.makeText(getBaseContext(), "delete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleEdit(Object o) {
                Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
            }

###### When the foreground was clicked do at least something

            @Override
            public void handleShow(Object o) {
                Toast.makeText(getApplicationContext(), "show", Toast.LENGTH_SHORT).show();
            }


###### Called when sorting is done

            @Override
            public void handleSort(List<ViewItem> list) {
                test = new ArrayList<>(list);
                updateAdapter();
                Toast.makeText(getApplicationContext(), "sort", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "Sort: " + isSortMode).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_ITEM_ITEM1:
                    
###### Switch to sort mode
                        this.isSortMode = !this.isSortMode;
                        item.setTitle("Sort: " + isSortMode);
                        updateData();
                        updateAdapter();
                        return true;

                    default:
                        return false;
                }
            }
        }
        
3. 

## Example

For a complete Example checkout: [https://github.com/cbgm/ListAPI_Example](https://github.com/cbgm/ListAPI_Example)
