package com.example.financeiro.api.security.util;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.financeiro.api.dto.GrupoDTO;

public class GeradorSenha {

	public static void main(String[] args) throws ParseException {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// System.out.println(encoder.encode("m0b1l30"));
		System.out.println("Passou senha:ddd: "+encoder.encode("(*$.re10per45mn)"));
	
		Stream<Integer> numbers = Stream.iterate(0, n -> n + 1 );		
		 numbers.limit(5).forEach(n -> System.out.println(n));
		
		System.out.println("Passou senha 2: "+encoder.encode("1"));
		
		String[] alphabet = new String[]{"AA", "CC","AA", "BB", "DD"};
		 
        // Convert String Array to List
        List<String> list = Arrays.asList(alphabet);

		List<GrupoDTO> listaDeGrupoDTO = new ArrayList<>();

        // A or B
        if (list.contains("AA")) {
            System.out.println("Hello A or B");
        }else {
        	System.out.println("diferente");
        }

        // A and B
        if (list.containsAll(Arrays.asList("AA", "BB"))) {
            System.out.println("Hello A and B");
        }

        // A and C
        if (list.containsAll(Arrays.asList("AA", "CC"))) {
            System.out.println("Hello A and C");
        }
		
	    System.out.println("--- Examples --- ");

        Duration oneHours = Duration.ofHours(24);
        System.out.println(oneHours.getSeconds() + " seconds");

        Duration oneHours2 = Duration.of(24, ChronoUnit.HOURS);
        System.out.println(oneHours2.getSeconds() + " seconds");

		// Test Duration.between
        System.out.println("\n--- Duration.between --- ");
        LocalDateTime today = LocalDateTime.now();
		
        
        LocalDateTime oldDate = LocalDateTime.of(2016, Month.AUGUST, 31, 10, 20, 55);
        LocalDateTime newDate = LocalDateTime.of(2016, Month.NOVEMBER, 9, 10, 21, 56);

        System.out.println(oldDate);
        System.out.println(newDate);

        //count seconds between dates
        Duration duration = Duration.between(oldDate, newDate);

        System.out.println(duration.getSeconds() + " seconds");
        
                
        System.out.println("--- Examples --- ");

        Period tenDays = Period.ofDays(1); 
        System.out.println(tenDays.getDays()); //1

        Period oneYearTwoMonthsThreeDays = Period.of(1, 2, 3);
        System.out.println(oneYearTwoMonthsThreeDays.getYears());   //1
        System.out.println(oneYearTwoMonthsThreeDays.getMonths());  //2
        System.out.println(oneYearTwoMonthsThreeDays.getDays());    //3

        System.out.println("\n--- Period.between --- ");
        LocalDate oldDatee = LocalDate.of(1982, Month.AUGUST, 31);
        LocalDate newDatee = LocalDate.of(2016, Month.NOVEMBER, 9);

        System.out.println(oldDate);
        System.out.println(newDate);

        // check period between dates
        Period period = Period.between(oldDatee, newDatee);

        System.out.print(period.getYears() + " years,");
        System.out.print(period.getMonths() + " months,");
        System.out.print(period.getDays() + " days");
        
        // LocalDateTime today = LocalDateTime.now();
		
        LocalDateTime newDateee = LocalDateTime.of(2016, Month.NOVEMBER, 9, 10, 21, 56);

        System.out.println(oldDate);
        System.out.println(newDate);

        // count between dates
        long years = ChronoUnit.YEARS.between(oldDate, newDate);
        long months = ChronoUnit.MONTHS.between(oldDate, newDate);
        long weeks = ChronoUnit.WEEKS.between(oldDate, newDate);
        long days = ChronoUnit.DAYS.between(oldDate, newDate);
        long hours = ChronoUnit.HOURS.between(oldDate, newDate);
        long minutes = ChronoUnit.MINUTES.between(oldDate, newDate);
        
        long seconds = ChronoUnit.SECONDS.between(oldDate, today);
        
        //Pega a data do Sistema
        LocalDate hoje = LocalDate.now();
        //Criando uma data anterior
        LocalDateTime ontem = LocalDateTime.of(2016, Month.NOVEMBER, 9, 10, 21, 56);


        //Pegar a Hora local
        LocalDateTime hora = LocalDateTime.now();

        //Comparando datas
        System.out.println("As datas são iguais? "+hoje.equals(ontem));
        //Comparando se já passou das 11:00
        System.out.println("Já passou das 11:00?" + hora.isAfter(LocalDateTime.of(2018, Month.NOVEMBER, 9, 10, 21, 56)));
		 	}

}
