import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Arrays;
import java.util.Properties;
import java.util.Comparator;

public class player3 implements ContestSubmission
{
    Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player3()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
	public void run() {
        // Run your algorithm here

        int evals = 0;

        int dim_size = 10;
        double min_boundary = -5.0;
        double max_boundary = 5.0;

        int population_size = 100;
        int number_of_parents = 50;


        Child[] population = new Child[population_size];
        for (int i = 0; i < population_size; i++) {
            Child new_child = new Child();

            double[] fenotype_values = new double[dim_size];
            for (int j = 0; j < dim_size; j++) {
                fenotype_values[j] = min_boundary + (max_boundary - min_boundary) * rnd_.nextDouble();
            }

            new_child.fenotype_value = fenotype_values;
            try {
                new_child.fitness = (double) evaluation_.evaluate(fenotype_values);
            } catch (NullPointerException e){
                return;
            }

            population[i] = new_child;
        }

//        for (int i=0;i<population_size;i++){
//            System.out.println(String.valueOf(i).concat(": ").concat(String.valueOf(population[i].fitness)));
//        }

        Arrays.sort(population, (child_1, child_2) -> Double.compare(child_2.fitness, child_1.fitness));

//        for (int i=0;i<population_size;i++){
//            System.out.println(String.valueOf(i).concat(": ").concat(String.valueOf(population[i].fitness)));
//        }


        while (evals < evaluations_limit_) {
            // Select parents
            Child[] top_parents = new Child[number_of_parents];
            for (int i = 0; i < number_of_parents; i++) {
                top_parents[i] = population[i];
            }
            Child[] new_children = new Child[number_of_parents];
            // Apply crossover / mutation operators

            for (int i = 0; i < number_of_parents; i++) {
                int mating_partner = rnd_.nextInt(number_of_parents);
                int crossover_point = rnd_.nextInt(dim_size);

                Child new_child = new Child();
//                System.out.println("CROSSOVERPOINT: ".concat(String.valueOf(crossover_point)));
//                System.out.println("MATINGPARTNER: ".concat(String.valueOf(mating_partner)));

                double[] fenotype_values = new double[dim_size];
                for (int j = 0; j < crossover_point; j++) {
//                    System.out.println("I: ".concat(String.valueOf(j)));
                    fenotype_values[j] = top_parents[i].fenotype_value[j];
                }
                for (int j = crossover_point; j < dim_size; j++) {
//                    System.out.println("J: ".concat(String.valueOf(j)));
                    fenotype_values[j] = top_parents[mating_partner].fenotype_value[j];
                }

                new_child.fenotype_value = fenotype_values;
                try {
                    new_child.fitness = (double) evaluation_.evaluate(fenotype_values);
                } catch (NullPointerException e){
                    return;
                }
                new_children[i] = new_child;
            }

            for (int i = 0; i < number_of_parents; i++) {
                population[population_size - i - 1] = new_children[i];
            }

            Arrays.sort(population, (child_1, child_2) -> Double.compare(child_2.fitness, child_1.fitness));

            double best_child[] = population[0].fenotype_value;
            // Check fitness of unknown fuction

            evals++;
            System.out.println("EVALS: ".concat(String.valueOf(evals)));

            for (int i=0;i<population_size;i++){
                System.out.println(String.valueOf(i).concat(": ").concat(String.valueOf(population[i].fitness)));
            }


            // Select survivors
        }
    }
}


class Child
{
    public double[] fenotype_value;
    public double fitness;
};