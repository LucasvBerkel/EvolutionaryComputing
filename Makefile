compile:
	javac --release 8 -cp contest.jar player3.java

submission:
	jar cmf MainClass.txt submission.jar player3.class Child.class

sphere:
	java -jar testrun.jar -submission=player3 -evaluation=SphereEvaluation -seed=1

bentcigar:
	java -jar testrun.jar -submission=player3 -evaluation=BentCigarFunction -seed=1

schaffers:
	java -jar testrun.jar -submission=player3 -evaluation=SchaffersEvaluation -seed=1

katsuura:
	java -jar testrun.jar -submission=player3 -evaluation=KatsuuraEvaluation -seed=1

all: compile submission sphere bentcigar schaffers katsuura

all_simple: compile submission sphere bentcigar schaffers

eval_simple:
	for number in 1 2 3 4 5 6 7 8 9 10; do java -jar testrun.jar -submission=player3 -evaluation=SphereEvaluation -seed=$$number ; done
	for number in 1 2 3 4 5 6 7 8 9 10; do java -jar testrun.jar -submission=player3 -evaluation=BentCigarFunction -seed=$$number ; done
	for number in 1 2 3 4 5 6 7 8 9 10; do java -jar testrun.jar -submission=player3 -evaluation=SchaffersEvaluation -seed=$$number ; done

eval_all: eval_simple
	for number in 1 2 3 4 5 6 7 8 9 10; do java -jar testrun.jar -submission=player3 -evaluation=KatsuuraEvaluation -seed=$$number ; done
