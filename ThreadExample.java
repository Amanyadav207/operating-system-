import java.util.concurrent.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.Array;

class HelloWorldPrinter extends Thread {
    @Override
    public void run(){
        System.out.println("Hello, World! " + Thread.currentThread().getName());
    }
}
class sorter implements Callable<ArrayList<Integer>> {
    ArrayList<Integer> listToSort;
    public sorter(ArrayList<Integer> list) {
        this.listToSort = list;
    }

    @Override
    public ArrayList<Integer> call() throws Exception {
        if(listToSort.size() <= 1){
            return listToSort;
        }
        int mid = listToSort.size() / 2;
        ArrayList<Integer> left =getsub(listToSort, 0, mid);
        ArrayList<Integer> right = getsub(listToSort, mid, listToSort.size());

        ExecutorService es = Executors.newFixedThreadPool(5);

        
        sorter leftSorter = new sorter(left);
        sorter rightSorter = new sorter(right);
        
        Future<ArrayList<Integer>> leftFuture = es.submit(leftSorter);
        Future<ArrayList<Integer>> rightFuture = es.submit(rightSorter);

        ArrayList<Integer> leftsorted = leftFuture.get();
        ArrayList<Integer> rightsorted = rightFuture.get();
        es.shutdown();
        return merge(leftsorted,rightsorted);
    }

    private ArrayList<Integer> merge(ArrayList<Integer> A,ArrayList<Integer> B){
        ArrayList<Integer> merged = new ArrayList<Integer>();
        int i = 0;
        int j = 0;
        while(i < A.size() && j < B.size()){
            if(A.get(i) < B.get(j)){
                merged.add(A.get(i));
                i++;
            }else{
                merged.add(B.get(j));
                j++;
            }
        }
        while(i < A.size()){
            merged.add(A.get(i));
            i++;
        }
        while(j < B.size()){
            merged.add(B.get(j));
            j++;
        }
        return merged;
    }



    private ArrayList<Integer> getsub(ArrayList<Integer> list, int start, int end){
        ArrayList<Integer> subList = new ArrayList<Integer>();
        for(int i = start; i < end; i++){
            subList.add(list.get(i));
        }
        return subList;
    }

}

class ArrayListModifier implements Callable<ArrayList<Integer>> {
    ArrayList<Integer> listToDouble;
    public ArrayListModifier(ArrayList<Integer> list) {
        this.listToDouble = list;
    }

    @Override
    public ArrayList<Integer> call() {
        ArrayList<Integer> listTo= new ArrayList<Integer>();
        for(int i = 0; i < listToDouble.size(); i++){
            listTo.add(listToDouble.get(i));
        }
        return listToDouble;
    }
}

class NumberPrinter extends Thread {
    int n;
    public NumberPrinter(int n) {
        this.n = n;
    }

    @Override
    public void run(){
        System.out.println(n + " " + Thread.currentThread().getName());
    }
}

public class ThreadExample {
    public static void main(String[] args) {
        // System.out.println("Main thread: " + Thread.currentThread().getName());

        // HelloWorldPrinter helloWorldPrinter1 = new HelloWorldPrinter();
        // helloWorldPrinter1.start();

        // HelloWorldPrinter helloWorldPrinter2 = new HelloWorldPrinter();
        // helloWorldPrinter2.start();

        // for(int i = 1; i <= 100; i++){
        //     NumberPrinter numberPrinter = new NumberPrinter(i);
        //     numberPrinter.start();
        // }
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(9,3,4,63,6,3,6,46,46,4));
        sorter sort = new sorter(list);
        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<ArrayList<Integer>> future = es.submit(sort);
        try {
            ArrayList<Integer> sortedList = future.get();
            System.out.println(sortedList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
}
