Hello Veda Vyas 90...
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prologlikesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author kevin
 */
public class PrologLikeSystem 
{
    /**
     * @param args the command line arguments
     */
    
    ArrayList<String> rule;    
    ArrayList<String> head;    
    ArrayList<ArrayList<String>> body;
    Stack<String> goal;
    HashMap<String, String> variable;

    public static void main(String[] args) 
    {         
        PrologLikeSystem pls = new PrologLikeSystem();
        System.out.println("PrologLikeSystem");
        pls.driver();
    }

    private void driver() 
    {     
        int i;
        ArrayList<String> al = new ArrayList();

        rule = new ArrayList();
        goal = new Stack();
        head = new ArrayList();
        body = new ArrayList();
        variable = new HashMap<String, String>();

        String dummy = "mortal(X) :- man(X)";
        rule.add(dummy);
        dummy = "man(socrates)";
        rule.add(dummy);
        dummy = "mortal(socrates)";
        al.add(dummy);
                
        populateHeadBody();     //split each rule into head and body         
        populateGoal(al);       //push subgoal into the stack

        while(!goal.empty())    //if goal is empty then return SUCCESS
        {
            String top = goal.peek();
            for(i=0;i<head.size();i++)
            {
                if(unify(head.get(i), top))
                    break;
            }
            if(i==head.size())  //no match with any of the heads, return FAILURE?
                return;      
            else            
                al = body.get(i);
                populateGoal(al);
        }    
        if(goal.empty())
            System.out.println("SUCCESS");
        else
            System.out.println("FAILURE");
    }
               
    public boolean unify(String h, String t) 
    {   
        int i;
        String[] a = h.split("\\W+");
        String[] b = t.split("\\W+");
        
        for(String x:a)
            System.out.println("unify1: " + x);
        
        for(String y:b)
            System.out.println("unify2: " + y);

        if(a.length == b.length) //both should be of same length
        {
         for(i=0;i<a.length;i++)
         {
             if(!isVariable(a[i]) && !isVariable(b[i])) //both E1 and E2 are constants
             {
                 if(!a[i].equals(b[i]))
                     return false;
             }
             else if(isVariable(a[i]) && !isVariable(b[i])) //E1 is a variable and E2 is a constant
             {
                 if(!variable.containsKey(a[i])) //checking if E1 already has a value
                     variable.put(a[i], b[i]);
                 else if(!variable.get(a[i]).equals(b[i])) //if it has, then it should be equal to E2
                     return false;                                 
             }
             else if(!isVariable(a[i]) && isVariable(b[i])) //E1 is a constant and E2 is a variable
             {
                 if(!variable.containsKey(b[i])) //checking if EE already has a value
                     variable.put(b[i], a[i]);
                 else if(!variable.get(b[i]).equals(a[i])) //if it has, then it should be equal to E1
                     return false;                                 
             }
             else //both B1 and B2 are variables
             {
                 //TO BE IMPLEMENTED
             }
         }
        }            
        return true;
    }

    public boolean isVariable(String s)
    {
        if(s.startsWith("[A-Z]", 1))
            return true;
        else
            return false;
    }
    
    private void populateHeadBody() 
    {
        for(String r : rule)
        {
            r = r.replaceAll("\\s+", "");            

            if(r.contains(":-"))
            {
                String[] retval = r.split(":-");
                head.add(retval[0]);
                
                ArrayList<String> al = new ArrayList();
                for(String s : retval[1].split(","))
                    al.add(s);
                body.add(al);
            }            
            else
            {
                head.add(r);
                body.add(new ArrayList());
            }            
        }        

        for(String h:head)
            System.out.println("head: " + h);

        for(ArrayList<String> b:body)
            for(String s:b)
                System.out.println("body: " + s);

    }

    public void populateGoal(ArrayList<String> subgoals)
    {
        for(String s : subgoals)
            goal.push(s);
    }
    
}
