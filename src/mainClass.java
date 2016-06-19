import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class mainClass {
	static int[][] train;
	static int[][] test;
	static int dataCount;
	static int TP;
	static int FP;
	static int TN;
	static int FN;
	public static void main(String[]args)
	{
		TP = 0;
		FP = 0;
		FN =0;
		TN = 0;
		loadDataset("Dataset.txt");
		loadTestDS("Dataset2.txt");
		System.out.print("Data Set Loaded!\n Svm Training. . . \n");
	  
		svm_model xvm = svmTrain();
		System.out.println("SVM Trained!");
		for(int i=0;i<test.length;i++)
		{
			evaluate(test[i], xvm);
			
		}
	    System.out.println("TP : "+ TP);
	    System.out.println("False P : "+ FP);
	    System.out.println("TN : "+ TN);
	    System.out.println("False N : "+ FN);
	    while(true)
	    {
	    	
	    }
		
	}
	public static void loadTestDS(String path) 
	{   System.out.println("Loading TestDS . . ");
		Scanner sc;
		try {
			sc = new Scanner(new File(path)).useDelimiter("\\Z");
			String text = sc.next();
			String[] lines =  text.split("\n");
			test = new int[lines.length][]; 
			for (int i=0;i<lines.length;i++)
			{
				String [] line = lines[i].split("  ");
				String[] features = line[1].split(" ");
 				int [] feature_vector = new int[545293];
				feature_vector [0] = Integer.parseInt(line[0]);
				
				for(int j=0;j<features.length;j++)
				{
					int index = Integer.parseInt(features[j].trim());
					//System.out.println(features[j]);
					feature_vector[index] =1;
				}
				test[i] = feature_vector;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File Not Found!");
		}
		
	}
	public static void loadDataset(String path) 
	{   System.out.println("Loading Dataset . . ");
		Scanner sc;
		try {
			sc = new Scanner(new File(path)).useDelimiter("\\Z");
			String text = sc.next();
			String[] lines =  text.split("\n");
			train = new int[lines.length][]; 
			dataCount = lines.length;
			for (int i=0;i<lines.length;i++)
			{
				String [] line = lines[i].split("  ");
				String[] features = line[1].split(" ");
 				int [] feature_vector = new int[545293];
				feature_vector [0] = Integer.parseInt(line[0]);
				
				for(int j=0;j<features.length;j++)
				{
					int index = Integer.parseInt(features[j].trim());
					//System.out.println(features[j]);
					feature_vector[index] =1;
				}
				train[i] = feature_vector;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File Not Found!");
		}
		
	}
	private static svm_model svmTrain() {
	    svm_problem prob = new svm_problem();
	    //set size of training dataset
	    prob.y = new double[dataCount];
	    prob.l = dataCount;
	    prob.x = new svm_node[dataCount][];     

	    for (int i = 0; i < dataCount; i++){  
	    	System.out.println("Iteration Number "+ i );
	        int[] features = train[i];
	        prob.x[i] = new svm_node[features.length-1];
	        for (int j = 1; j < features.length; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = features[j];
	            prob.x[i][j-1] = node;
	        }           
	        prob.y[i] = features[0];
	    }               

	    svm_parameter param = new svm_parameter();
	    param.probability = 1;
	    param.gamma = 0.5;
	    param.nu = 0.5;
	    param.C = 1;
	    param.svm_type = svm_parameter.C_SVC;
	    param.kernel_type = svm_parameter.LINEAR;      
	    param.cache_size = 20000;
	    param.eps = 0.001;      

	    svm_model model = svm.svm_train(prob, param);
	    return model;
	}
	public static double evaluate(int[] features, svm_model model) 
	{
	    svm_node[] nodes = new svm_node[features.length-1];
	    for (int i = 1; i < features.length; i++)
	    {
	        svm_node node = new svm_node();
	        node.index = i;
	        node.value = features[i];

	        nodes[i-1] = node;
	    }

	    int totalClasses = 2;       
	    int[] labels = new int[totalClasses];
	    svm.svm_get_labels(model,labels);

	    double[] prob_estimates = new double[totalClasses];
	    double v = svm.svm_predict_probability(model, nodes, prob_estimates);
	    System.out.println("(Actual:" + features[0] + " Prediction:" + v + ")");
	    if(features[0]==1)
	    {
	    	if(features[0]==v)
		    	   TP++;
		    	else
		    		FN++;
	    }else if(features[0]==0)
	    {
	    	if(features[0]==v)
		    	   TN++;
		    	else
		    		FP++;
	    }
        
	    return v;
	}

	
}
