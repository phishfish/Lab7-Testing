/**
 *
 *@invariants
 *
 *
 *@correspondence
 *
 *
 */

public class Mortgage extends AbsMortgage implements IMortgage{

	private double homeCost;
	private double downPayment;
	private int years;
	private ICustomer customer;
	public Mortgage(double cost, double down, int length, ICustomer person){
		homeCost = cost;
		downPayment = down;
		years = length;
		customer = person;
	}

	public boolean loanApproved(){
		if(getRate() > .1){
			return false;
		}

		if(downPayment < homeCost * MIN_PERCENT_DOWN){
			return false;
		}

		if((customer.getMonthlyPay() * years)/(customer.getIncome() * years) > 0.40){
			return false;
		}

		return true;
	}

	public double getPayment(){
		double interestRate = getRate();
		return (interestRate * getPrincipal())/(1 - Math.pow(1 + rate, -1 * years * 12));
	}

	public double getRate(){
		double apr = BASERATE;
		if(years < MAX_YEARS)
			apr += 0.005;
		else
			apr += 0.01;

		if(homeCost * PREFERRED_PERCENT_DOWN < downPayment)
			apr += 0.05;

		int creditScore = customer.getCreditScore();
		if(creditScore < BADCREDIT)
			apr += VERYBADRATEADD;
		else if(creditScore < FAIRCREDIT)
			apr += BADRATEADD;
		else if(creditScore < GOODCREDIT)
			apr += FAIRRATEADD;
		else if(creditScore < GREATCREDIT)
			apr += GOODRATEADD;

		return apr;
	}

	public double getPrincipal(){
		return homeCost - downPayment;
	}
public int getYears(){
		return years;
	}


}
