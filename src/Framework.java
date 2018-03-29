import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;

/*** Some Inspiration for this algorithm came from this paper: 
 * "Minimum and Worst-Case Performance Ratios of Rollout Algorithms" by 
 * Luca Bertazzi
 * 
 * @author jonathan
 *
 */

public class Framework {
    int n;
    int v[];
    int w[];
    int W;
    boolean picked[];
    
    void input(String input_name){
        File file = new File(input_name);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            String text = reader.readLine();
            String parts[];
            parts=text.split(" ");
            n=Integer.parseInt(parts[0]);
            W=Integer.parseInt(parts[1]);
            v=new int[n];
            w=new int[n];
            picked=new boolean[n];
            for (int i=0;i<n;i++){
                text=reader.readLine();
                parts=text.split(" ");
                v[i]=Integer.parseInt(parts[0]);
                w[i]=Integer.parseInt(parts[1]);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //writing the output
    void output(String output_name)
    {
        try{
            PrintWriter writer = new PrintWriter(output_name, "UTF-8");

            int total_v=0;
            for (int i=0;i<n;i++)
              if (picked[i])
                total_v += v[i];
            writer.println(total_v);
            for (int i=0;i<n;i++)
              if (picked[i])
                writer.println("1");
              else 
                writer.println("0");

            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Framework(String []Args){
        input(Args[0]);
        
        //YOUR CODE GOES HERE
        
        /*** An inner class that is sorted based on the weights
         * 
         * @author jonathan
         *
         */
        class item_data_w implements Comparable<item_data_w> {
        	private int weight;
        	private int value;
        	private int index;
        	private boolean[] choose_w = new boolean[n];
        	
        	private item_data_w(int w, int v, int i) {
        		weight = w;
        		value = v;
        		index = i;
        	}
        	
            
            /*** Rollout for the objects sorted by increasing order of value
             * 
             * @param objects_1
             * @param objects_2
             * @return
             */
            public int super_w(item_data_w[] objects,int capacity) {
            	int value = -1;
            	for (int i = 0; i < objects.length; i++) {
            		Object[] temp = greedy_w(objects, capacity - objects[i].weight, objects[i].index, i);
            		if (objects[i].value + (Integer) temp[0] > value) {
            			this.choose_w = (boolean[]) temp[1];
            			value = objects[i].value +  (Integer) temp[0];
            		}
            	}
            	return value;
            }
            
            /*** Add items until you cannot add any more items by weight
             * 
             * @param values
             * @param weights
             * @param capacity
             * @return
             */
            public Object[] greedy_w(item_data_w[] objects, int capacity, int guaranteed, int j) {
            	int w = capacity;
            	int v = 0;
            	int i = 0;
            	boolean weights[] = new boolean[n];
            	weights[guaranteed] = true;
            	int old_weight = objects[j].weight;
            	int old_value = objects[j].value;
            	objects[j].weight = 0;
            	objects[j].value = 0;
            	while (w >= 0 && i < objects.length) {
            		if (objects[i].weight <= w) {
            			v += objects[i].value;
            			w -= objects[i].weight;
            			weights[objects[i].index] = true;
            			i += 1;
            		} else {
            			objects[j].weight = old_weight;
                    	objects[j].value = old_value;
            			break;
            		}
            	}
            	return new Object[] {v, weights};
            }
        	
        	/*** Compares two elements solely based on the numerical values of its "weight" elements
        	 * 
        	 */
    		@Override
    		public int compareTo(item_data_w o) {
    			// TODO Auto-generated method stub
    			if (value < o.value) {
    				return 1;
    			} else if (value == o.value) {
    				return 0;
    			} else {
    				return -1;
    			}
    		}
        }
        
        /*** An inner class that is sorted based on the ratio of value/weight
         * 
         * @author jonathan
         *
         */
        class item_data_r implements Comparable<item_data_r> {
        	private int weight;
        	private int value;
        	private int index;
        	private float ratio;
        	private boolean[] choose_r = new boolean[n];
        	
        	private item_data_r(int w, int v, int i) {
        		weight = w;
        		value = v;
        		index = i;
        		ratio = ((float) v) / ((float) w);
        	}
        	
        	/*** Rollout for the objects sorted by ratio
             * 
             * @param objects_1
             * @param objects_2
             * @return
             */
            public int super_r(item_data_r[] objects,int capacity) {
            	int value = -1;
            	for (int i = 0; i < objects.length; i++) {
            		Object[] temp = greedy_r(objects, capacity - objects[i].weight, objects[i].index, i);
            		if (objects[i].value + (Integer) temp[0] > value) {
            			this.choose_r = (boolean[]) temp[1];
            			value = objects[i].value +  (Integer) temp[0];
            		}
            	}
            	return value;
            }
            
            /*** Add items until you cannot add any more items by ratio
             * 
             * @param values
             * @param weights
             * @param capacity
             * @return
             */
            public Object[] greedy_r(item_data_r[] objects, int capacity, int guaranteed, int j) {
            	int w = capacity;
            	int v = 0;
            	int i = 0;
            	boolean ratios[] = new boolean[n];
            	ratios[guaranteed] = true;
            	int old_weight = objects[j].weight;
            	int old_value = objects[j].value;
            	objects[j].weight = 0;
            	objects[j].value = 0;
            	while (w >= 0 && i < objects.length) {
            		if (objects[i].weight <= w) {
            			v += objects[i].value;
            			w -= objects[i].weight;
            			ratios[objects[i].index] = true;
            			i += 1;
            		} else {
            			objects[j].weight = old_weight;
            			objects[j].value = old_value;
            			break;
            		}
            	}
            	return new Object[] {v, ratios};
            }
            
        	/*** Compares two elements solely based on the numerical values of its "ratio" elements
        	 * 
        	 */
    		@Override
    		public int compareTo(item_data_r o) {
    			// TODO Auto-generated method stub
    			if (ratio < o.ratio) {
    				return 1;
    			} else if (ratio == o.ratio) {
    				return 0;
    			} else {
    				return -1;
    			}
    		}
        }
        item_data_w w_1 = new item_data_w(0,0,0);
        item_data_r r_1 = new item_data_r(0,0,0);
        
        item_data_w[] by_weight = new item_data_w[n];
        item_data_r[] by_ratio = new item_data_r[n];
        
        for (int i = 0; i < n; i++) {
        	by_weight[i] = new item_data_w(w[i],v[i], i);
        	by_ratio[i] = new item_data_r(w[i],v[i], i);
        }
        
        Arrays.sort(by_weight);
        Arrays.sort(by_ratio);
        
        if (w_1.super_w(by_weight, W) > r_1.super_r(by_ratio, W)) {
        	picked = w_1.choose_w;
        } else {
        	picked = r_1.choose_r;
        }
        
        //END OF YOUR CODE

        output(Args[1]);
    }
    
    public static void main(String [] Args) //Strings in Args are the name of the input file followed by the name of the output file
    {
        new Framework(Args);
    }
}
