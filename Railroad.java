/* Bryce Green
 * Dr. Steinberg
 * COP 3503 Spring 2023
 * Programming Assignment 5
 */
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Railroad{
    private int tracks;  //represents number of tracks (edges)
    private String name;  //represents string object that holds the file name
    private int[] weights;  //the array that holds the weights for each track
    private String[] u;  //the array of string objects that hold each source
    private String[] v;  //the array of string objects that hold each destination
    private HashMap<Integer, String> map1;  //hashmap stores vertex names
    private int vertexNum;  //the amount of unique vertices in the program
    private LinkedList<Track> trackList;  //linked list holds the edges and weights between each vertex
    
    public Railroad(int tracks, String name){
        this.tracks = tracks;
        this.name = name;
        u = new String[tracks];
        v = new String[tracks];
        weights = new int[tracks];
        map1 = new HashMap<>();
        
        trackList = new LinkedList<Track>();
        read(name);
    }
    
    public void read(String name){
        File obj = new File(name);  //create new file object for greed file
        Scanner fileio = null;
        
        try{
            fileio = new Scanner(obj);
        }
        catch(FileNotFoundException e){  //print error if file doesn't open properly
            System.out.println("Error occurred while opening file");
            e.printStackTrace();
        }
        
        int k = 0;
        int l = 0;
        
        while(fileio.hasNextLine()){
            String str = fileio.nextLine();
            
            char[] line = str.toCharArray();
            ArrayList<Character> vertex1 = new ArrayList<Character>();  //holds first vertex from file
            ArrayList<Character> vertex2 = new ArrayList<Character>();  //holds second vertex from file
            ArrayList<Character> num = new ArrayList<Character>();  //holds weight from file
            String number;
            
            int i = 0;
            int j = 0;
            
            while(line[i] != ' '){  //scan in first vertex
                vertex1.add(line[i]);
                i++;
                j++;
            }
                
            i++;
            j = 0;
                
            while(line[i] != ' '){  //scan in second vertex
                vertex2.add(line[i]);
                i++;
                j++;
            }
                
            i++;
            j = 0;
                
            while(i < line.length){  //scan in weight
                num.add(line[i]);
                i++;
                j++;
            }
            
            u[k] = toString(vertex1);
            v[k] = toString(vertex2);
            
            if(!map1.containsValue(u[k])){  //inserts vertex 1 into map1 if it doesn't already exist
                map1.put(l, u[k]);
                l++;
                vertexNum++;
            }
            
            if(!map1.containsValue(v[k])){  //inserts vertex 2 into map1 if it doesn't already exist.
                map1.put(l, v[k]);
                l++;
                vertexNum++;
            }
                
            
            try{  //handles possible NumberFormatException
                weights[k] = Integer.parseInt(toString(num));
            }
            catch(NumberFormatException e){
                System.out.println("Invalid number\n");
            }
            
            k++;
        }
    }
    
    public String buildRailroad(){
        
        DisjointSet graph = new DisjointSet(map1, vertexNum);  //make disjoint set for each vertex of set V
       
        mergeSort(u, v, weights, tracks);  //sort each edge based on weight using merge sort
        
        int sum = 0;
        String v1;  //holds vertex that lexicographically comes first
        String v2;  //holds vertex that lexicographically comes second
        
        for(int i = 0; i < weights.length; i++){  //traverses through weights list
        
            if(!graph.find(u[i]).equals(graph.find(v[i]))){  //if u and v already exist in the same disjoint set
                graph.union(u[i], v[i]);  //put u and v in the same disjoint set
                if(u[i].compareTo(v[i]) < 0){  //checks if u or v is lexicographically first
                    v1 = u[i];
                    v2 = v[i];
                }
                else{
                    v1 = v[i];
                    v2 = u[i];
                }
                Track e = new Track(v1, v2, weights[i]);  //create new edge
                trackList.add(e);  //add edge to linked list
            }
        }
        
        return convertToString(trackList);  //return output
    }
    
    public void mergeSort(String[] u, String[] v, int[] weights, int tracks){
        if (tracks < 2)  //sort array until it is separated completely
            return;
        
        int mid = tracks / 2;  //indicates the middle of arrays
        String[] leftU = new String[mid];  //initializes the left subarray for u
        String[] leftV = new String[mid];  //initializes the left subarray for v
        int[] leftW = new int[mid];  //initializes the left subarray for weights
        String[] rightU = new String[tracks - mid];  //initializes the right subarray for u
        String[] rightV = new String[tracks - mid];  //initializes the right subarray for v
        int[] rightW = new int[tracks - mid];  //initializes the right subarray for weights
        
        for(int i = 0; i < mid; i++){ //assign values to each left subarray
            leftU[i] = u[i];
            leftV[i] = v[i];
            leftW[i] = weights[i];
        }
        
        for(int i = mid; i < tracks; i++){  //assign values to each right subarray
            rightU[i - mid] = u[i];
            rightV[i - mid] = v[i];
            rightW[i - mid] = weights[i];
        }
        
        mergeSort(leftU, leftV, leftW, mid);  //recursively break apart left arrays
        mergeSort(rightU, rightV, rightW, tracks - mid);  //recursively break apart right arrays
    
        merge(u, v, weights, leftU, leftV, leftW, rightU, rightV, rightW, mid, tracks - mid);  //merge all sorted arrays back together
    }
    
    public void merge(String[] u, String[] v, int[] weights, String[] leftU, String[] leftV, int[] leftW, String[] rightU, String[] rightV, int[] rightW, int lHalf, int rHalf){
        
        int a = 0; //for left arrays
        int b = 0; //for right arrays
        int c = 0; //for main arrays
        
        
        while (a < lHalf && b < rHalf){  //sort separate elements of the arrays
            if (leftW[a] <= rightW[b]){
                u[c++] = leftU[a++];  //assigns string from left u array to main u array
                c--;
                a--;
                v[c++] = leftV[a++];  //assigns string from left v array to main v array
                c--;
                a--;
                weights[c++] = leftW[a++];  //assigns number from left weights array to main weights array
            }
            else{
                u[c++] = rightU[b++];  //assigns string from right u array to main u array
                c--;
                b--;
                v[c++] = rightV[b++];  //assigns string from right v array to main v array
                c--;
                b--;
                weights[c++] = rightW[b++];  //assigns string from left weights array to main weights array
            } 
        }
        
        while (a < lHalf){  //merge left array values into big array
            u[c++] = leftU[a++];  
            c--;
            a--;
            v[c++] = leftV[a++];
            c--;
            a--;
            weights[c++] = leftW[a++];
        }
        
        while (b < rHalf){  //merge right array values into big array
            u[c++] = rightU[b++];
            c--;
            b--;
            v[c++] = rightV[b++];
            c--;
            b--;
            weights[c++] = rightW[b++];
        }
    }
    
    public String toString(ArrayList<Character> vertex){
        StringBuilder str = new StringBuilder(vertex.size());
        
        for(Character c : vertex)  //append each character in arraylist
            str.append(c);
        
        return str.toString();  //return new string
    }
    
    public String convertToString(LinkedList<Track> trackList){
        int sum = 0;
        StringBuilder str2 = new StringBuilder(trackList.size() + 1);
        
        for(int i = 0; i < trackList.size(); i++){
            Track temp = trackList.get(i);  //looks at each edge in linked list
            str2.append(temp.source + "---" + temp.dest + "\t$" + String.valueOf(temp.cost) + "\n");
            sum = sum + temp.cost;  //takes the sum of all weights in the linked list
        }
        
        str2.append("The cost of the railroad is $" + String.valueOf(sum) +".");
        
        return str2.toString();
    }
}

//this class sets up a track for the railroad system
class Track{
    String source;
    String dest;
    int cost;
    
    public Track(String source, String dest, int cost){
        this.source = source;
        this.dest = dest;
        this.cost = cost;
    }
}

class DisjointSet{
    private HashMap<Integer, String> djMap;  //new hashmap
    private int vertexNum;  //holds number of vertices
    private String[] rep;  //representatives of disjoint sets
    private int[] rank;  //ranks of nodes in disjoint sets
    
    public DisjointSet(HashMap<Integer, String> map1, int vertexNum){
        this.vertexNum = vertexNum;
        rep = new String[vertexNum];
        rank = new int[vertexNum];
        djMap = new HashMap<>();
        djMap.putAll(map1);  //copy values from map1 into djmap
        makeSet();
    }
    
    public void makeSet(){
        for(int i = 0; i < vertexNum; i++){  //make disjoint set for each unique vertex in program
            rep[i] = djMap.get(i);
        }
    }
    
    public String find(String x){
        
        Integer i = 0;
        
        for(Integer j : djMap.keySet()){  //looks for key associated with x
            if(djMap.get(j).equals(x)){
                i = j;
                break;
            }
        }
        if(!rep[i].equals(x))  //checks if the representative of i is equal to x
                rep[i] = find(rep[i]);  //path compression
                
        return rep[i];  //return current representative
    }
    
    public void union(String n, String m){
        String xRep = find(n);  //representative of n
        String yRep = find(m);  //representative of m
        Integer v1 = 0;
        Integer v2 = 0;
        
        for(Integer i : djMap.keySet()){  //looks for key in hashmap associated with xRep
            if(djMap.get(i).equals(xRep)){
                v1 = i;
                break;
            }
        }
        
        for(Integer i : djMap.keySet()){  //looks for key in hashmap associated with yRep
            if(djMap.get(i).equals(yRep)){
                v2 = i;
                break;
            }
        }
        
        if(rank[v1] < rank[v2])  //checks if the rank of xRep is less than the rank of yRep
            rep[v1] = yRep;  //the representative of xRep now represents yRep
        else if(rank[v2] < rank[v1])  //checks if the rank of yRep is less than the rank of xRep
            rep[v2] = xRep;  //the representative of xRep now represents yRep
        else{
            rep[v2] = xRep;  //the representative of yRep now represents xRep
            rank[v1]++;  //increase xRep's rank by 1
        }
    }
}
