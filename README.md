# ListAPI

An API for the easy usage of an **Android** configureable ListView with the features of a **sort mechanism** and adding fast **custom buttons with slide function**.
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

        public MyViewHolder setUpView(final int position, View convertView, final ViewGroup parent, final CBListMode mode, final ICBActionNotifier listMenuListener, final int highlightPos, final LayoutInflater inflater, final CBSwipeListener swipeListener, Context context) {

        MyViewHolder test = (MyViewHolder)holder;
        test.name.setText(item);
        test.name.setEnabled(true);
        test.name.setTextColor(Color.GREEN);
        return test
        }

###### Init the the layout to the holder

        public MyHolder initView(View itemView, final Context context) {
            MyHolder test = (MyHolder)holder;
            test.name = (TextView) itemView.findViewById(R.id.txt_dynamic2);

            return test;
        }
        
2. Define the Adapter which connects the list and extend from CBAdapter
        
        public class MyAdapter extends CBAdapter<ViewItem> {
            public MyAdapter(Context context, List<ViewItem> data, CBListMode mode) {
                super(context, data, mode);
            }
        }

3. Define the Activity which uses the list and extend from CBActivity

        public class MyFragment extends Fragment implements ICBActionDelegate, MyMenuListener {
            List<ViewItem> test;
            CBListView listContainer;
            MyAdapter adapter;
            Boolean isSortMode = false;
            private static final int MENU_ITEM_ITEM1 = 1;
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                View rootView = inflater.inflate(R.layout.fragment, container, false);
                this.listContainer = (CBListView) rootView.findViewById(R.id.list_container);
                this.listContainer.setDelegateListener(this);
                this.adapter = new MyAdapter(getContext(), loadData(), CBListMode.SWIPE);
                setHasOptionsMenu(true);
                this.listContainer.init(CBListMode.SWIPE, loadData(), adapter);
                return rootView;

            }

            public List loadData() {
                this.test = new ArrayList<>();
                int type = 1;
                test = new ArrayList<>();
                for (int i = 0; i < 20; i++) {

                    if (type == 1) {
                        String item = "item 11111111111" + i;
                    
###### Add list elements (needs the data, the holder and the layout)

                    MyListViewItem li = new MyListViewItem(item, new MyViewHolder(), R.layout.backitem_standard, this, -1);
                    test.add(li);
                }
                return test;
            }

###### When the foreground was clicked do at least something

            @Override
            public void delegateSingleClick(int position) {

            }

###### Called when sorting is done

            @Override
            public void delegateSort(List list) {

            }
        }     
4. Switch the list mode

        this.listContainer.init(CBListMode.SORT, loadData(), adapter);

        
5. 

## Example

For a complete Example checkout: [https://github.com/cbgm/ListAPI_Example](https://github.com/cbgm/ListAPI_Example)

## License

Licensed under the MIT license. (http://opensource.org/licenses/MIT)
