package com.redhat.demo.qlb;

import java.util.ArrayList;
import java.util.List;

import com.redhat.demo.qlb.loan_application.model.Applicant;
import com.redhat.demo.qlb.loan_application.model.Loan;
import org.kie.api.KieBase;
import org.kie.api.KieServices;

import org.kie.api.command.KieCommands;
import org.kie.api.command.Command;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String STATELESS_KIE_SESSION_ID = "default-stateless-ksession";
    private static final String DECISION_FLOW_ID = "loan-application.decision-flow";


    public static void main(String[] args) {

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieBase kieBase = kieContainer.getKieBase();

        StatelessKieSession statelessKieSession = kieBase.newStatelessKieSession();

        KieCommands kieCommands = kieServices.getCommands();
        List<Command> commands = new ArrayList<Command>();
        Applicant applicant = getApplicant();
        Loan loan = getLoan();
        commands.add( kieCommands.newInsert(applicant));
        commands.add(CommandFactory.newInsert(loan));
        commands.add(CommandFactory.newStartProcess(DECISION_FLOW_ID));

        statelessKieSession.execute( kieCommands.newBatchExecution( commands,STATELESS_KIE_SESSION_ID ) );

        LOGGER.info(applicant.toString());
        LOGGER.info(loan.toString());
    }



     private static Applicant getApplicant() {
         Applicant applicant = new Applicant();
         applicant.setName("Lucien Bramard");
         applicant.setAge(34);
         applicant.setYearlyIncome(90000);
         applicant.setCreditScore(410);
         return applicant;
     }

    private static Loan getLoan() {
        Loan loan = new Loan();
        loan.setAmount(280000);
        loan.setDuration(25);
        return loan;
    }

}