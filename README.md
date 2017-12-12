# ListAPI

An API for the easy usage of an **Android** configureable RecyclerView with the features of a **sort mechanism** and adding fast **custom buttons with slide function**.
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

        public class MyHolder extends CBViewHolder<BaseItem> {
                public TextView name;

                public MyHolder(View itemView, Context context, ViewGroup parent, int itemRessource) {
                        super(itemView, context, parent, itemRessource, true, true);
                }

##### Init the view elements
                @Override
                public void initPersonalView(View itemView, Context context) {
                        this.name = (TextView) itemView.findViewById(R.id.txt_dynamic2);
                }
##### Init custom buttons

                @Override
                protected void initCustomButtons() {
                        this.customButtons.add(new MyButton(CustomLayoutID.CUSTOMBUTTON_ID, R.color.yellow, -1));
                }

##### Setup view funtionality
                @Override
                protected void setUpPersonalView(BaseItem listObject, final int position, ICBActionNotifier<BaseItem> actionNotifier, Context context) {
                        final FirstItem temp = (FirstItem) listObject;
                        this.name.setText(temp.getTest());
                        this.name.setEnabled(true);
                        this.name.setTextColor(Color.GREEN);

                        this.buttonContainer.findViewById(CustomLayoutID.CUSTOMBUTTON_ID).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        if (CBModeHelper.getInstance().isItemTouchCurrentItem(position))
                                                myMenuListener.test(temp);
                                }
                        });
                }
        
2. Define the Adapter which connects the list and extend from CBAdapter
        
                public class MyAdapter extends CBAdapter<MyHolder, BaseItem> {

                        private MyMenuListener myMenuListener;

                        public MyAdapter(Context context) {
                                super(context);
                        }


                        @Override
                        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                return new MyViewHolder(CBBaseView.getView(context), context, parent, R.layout.backitem_standard);
                        }

              
                }
3. Define the Fragment which uses the list

                public class MyFragment extends Fragment implements ICBActionDelegate<BaseItem> {
                        private List<BaseItem> viewItems;
                        private CBListView<MyHolder, BaseItem, MyAdapter> listContainer;
                        private MyAdapter adapter;

                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                                View rootView = inflater.inflate(R.layout.fragment, container, false);
                                CBModeHelper.getInstance().setListMode(CBListMode.SWIPE);
                                this.listContainer = find(rootView, R.id.list_container);
                                this.listContainer.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                this.listContainer.setLayoutManager(llm);
                                this.listContainer.setDelegateListener(this);
                                this.adapter = new MyAdapter(getContext());
                                this.adapter.setCustomMenuListener(this);
                                loadData();
                                setHasOptionsMenu(true);
##### Add the listts data
                                this.listContainer.init(this.viewItems, this.adapter);
                                return rootView;
                        }
                        ......

###### When the foreground was clicked do at least something

            @Override
            public void delegateSingleClick(int position) {

            }

###### Called when sorting is done

            @Override
            public void delegateSort(List list) {

            }
        }     
4. Switch from swipe / select to sort list mode

         CBModeHelper.getInstance().setListMode(CBListMode.SORT);
         loadData();
         this.listContainer.init(this.viewItems, this.adapter);

        
5. 

## Example

For a complete Example checkout: [https://github.com/cbgm/ListAPI_Example](https://github.com/cbgm/ListAPI_Example)

## License

Licensed under the MIT license. (http://opensource.org/licenses/MIT)
