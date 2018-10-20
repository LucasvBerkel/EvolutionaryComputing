import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Arrays;
import java.util.Properties;
import java.util.Comparator;
import java.util.ArrayList;

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

        int population_size = 50;
        int number_of_parents = 40;
        int number_of_children = 210;
        int tournament_size = 10;
        double tournament_start_p = 0.5;
        double tau_ap = 0.158;
        double tau = 0.397;
        double s = 2;
        double mutation_threshold = 0.05;

        // Declaring some variables we might need later
        int i, j;
        int random_pick;
        double tournament_p;
        Child[] tournament;

        // Init population
        Child[] population = new Child[population_size];
        for (i = 0; i < population_size; i++) {
            Child new_child = new Child();

            double[] phenotype_values = new double[dim_size];
            double[] sigma_values = new double[dim_size];
            for (j = 0; j < dim_size; j++) {
                phenotype_values[j] = min_boundary + (max_boundary - min_boundary) * rnd_.nextDouble();
                sigma_values[j] = rnd_.nextGaussian();
            }

            new_child.phenotype_value = phenotype_values;
            new_child.sigma_value = sigma_values;
            try {
                new_child.fitness = (double) evaluation_.evaluate(phenotype_values);
            } catch (NullPointerException e){
                return;
            }

            population[i] = new_child;
        }

        // Sort population based on fitness
        Arrays.sort(population, (child_1, child_2) -> Double.compare(child_2.fitness, child_1.fitness));

        while (evals < evaluations_limit_) {
            ///////////////////////////////////PARENT SELECTION///////////////////////////////////
            Child[] parents = new Child[number_of_parents];
            ArrayList<Child> next_generation = new ArrayList<Child>();
            next_generation.addAll(Arrays.asList(population));

            // Ranking selection; exponential ranking
            double su=0;
            for ( i = 1; i<=number_of_parents; i++){
                su = su + Math.exp(-i);
            }

            double c = 1/(number_of_parents - su);

            for(i =0 ; i<number_of_parents; i++){
                int gen_size = next_generation.size();
                double[] prob_values = new double[gen_size];
                for(j = 0; j < gen_size; j++){
                    prob_values[j] = (1-Math.exp(-1*(gen_size - j - 1)))/(c);
                }
                double random_prob = rnd_.nextDouble();
                j = 0;
                double counter = prob_values[j];
                while(counter < random_prob){
                    j++;
                    counter += prob_values[j];
                }
                parents[i] = next_generation.get(j);
                next_generation.remove(j);
            }

            /////////////////////////////////// RECOMBINATION AND MUTATION ///////////////////////////////////
            Child[] new_children = new Child[number_of_children];
            for (i = 0; i < number_of_children; i++) {

                /////////////////////////////////// RECOMBINATION ///////////////////////////////////
                // Uniform crossover
                int mating_partner1 = rnd_.nextInt(number_of_parents);
                int mating_partner2 = rnd_.nextInt(number_of_parents);

                Child new_child = new Child();
                double[] phenotype_values = new double[dim_size];
                double[] sigma_values = new double[dim_size];
                for (j = 0; j < dim_size; j++){
                    if(rnd_.nextDouble() > 0.5){
                        phenotype_values[j] = parents[mating_partner1].phenotype_value[j];
                        sigma_values[j] = parents[mating_partner1].sigma_value[j];
                    } else {
                        phenotype_values[j] = parents[mating_partner2].phenotype_value[j];
                        sigma_values[j] = parents[mating_partner2].sigma_value[j];
                    }
                }
                new_child.phenotype_value = phenotype_values;
                new_child.sigma_value = sigma_values;

                /////////////////////////////////// MUTATION ///////////////////////////////////
                // Uncorrelated mutation with n sigma's
                phenotype_values = new double[dim_size];
                sigma_values = new double[dim_size];
                double overall_learning_rate = Math.exp(tau_ap * rnd_.nextGaussian());
                double nextt = 0;
                double y1 = 0;
                double sigma1 = 0;

                for (j = 0; j < dim_size; j++) {


                        //1-st option to update sigma

                    do {
                        sigma_values[j] = new_child.sigma_value[j] * overall_learning_rate * Math.exp(tau * rnd_.nextGaussian());
                    }while(sigma_values[j] > 3/2);

                        //2-nd option to update sigma
                        //sigma_values[j] = 1 - 0.999*evals/T;


                        //Normal
                    /*
                    do {
                        phenotype_values[j] = new_child.phenotype_value[j] + rnd_.nextGaussian() * sigma_values[j];
                    }while(phenotype_values[j] > 15 || phenotype_values[j]<-15);
                    */

                        //Cauchy
                    /*
                    do {
                        phenotype_values[j] = new_child.phenotype_value[j] + sigma_values[j]*Math.tan((rnd_.nextDouble()-0.5) * Math.PI);
                    }while(phenotype_values[j] > 15 || phenotype_values[j]<-15);
                    */

                        //Student with 2 degrees of freedom
                    do {
                        phenotype_values[j] = new_child.phenotype_value[j] + sigma_values[j]*(rnd_.nextDouble() - 0.5)*Math.sqrt(2/(rnd_.nextDouble()*(1-rnd_.nextDouble())));
                    }while(phenotype_values[j] > 15 || phenotype_values[j]<-15);

                    phenotype_values[j] = new_child.phenotype_value[j] + rnd_.nextGaussian() * sigma_values[j];
                }
                new_child.phenotype_value = phenotype_values;
                new_child.sigma_value = sigma_values;
                try {
                    new_child.fitness = (double) evaluation_.evaluate(phenotype_values);
                } catch (NullPointerException e){
                    return;
                }
                new_children[i] = new_child;
            }

            /////////////////////////////////// SURVIVOR SELECTION ///////////////////////////////////
            // Tournament selection
            ArrayList<Child> off_spring = new ArrayList<Child>();
            off_spring.addAll(Arrays.asList(parents));
            off_spring.addAll(Arrays.asList(new_children));
            i = 0;
            while(i < number_of_parents){
                tournament = new Child[tournament_size];
                for(j = 0; j < tournament_size; j++){
                    random_pick = rnd_.nextInt(off_spring.size()-1);
                    tournament[j] = off_spring.get(random_pick);
                    off_spring.remove(random_pick);
                }
                Arrays.sort(tournament, (child_1, child_2) -> Double.compare(child_2.fitness, child_1.fitness));
                tournament_p = tournament_start_p;
                for(j = 0; j < tournament_size; j++){
                    tournament_p = tournament_p * Math.pow(1-tournament_p, j);
                    if(rnd_.nextDouble() < tournament_p){
                        next_generation.add(tournament[j]);
                        i++;
                        break;
                    }
                }
                for(int k = 0; k < tournament_size; k++){
                    if(k != j) {
                        off_spring.add(tournament[k]);
                    }
                }
            }

            population = next_generation.toArray(new Child[population_size]);

            // Sort population for next evaluation
            Arrays.sort(population, (child_1, child_2) -> Double.compare(child_2.fitness, child_1.fitness));

            double best_child[] = population[0].phenotype_value;

            evals++;
        }
    }
}


class Child
{
    public double[] phenotype_value;
    public double[] sigma_value;
    public double fitness;
};