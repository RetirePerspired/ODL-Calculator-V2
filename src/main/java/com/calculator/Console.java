package com.calculator;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class Console {
/*Parking Lot
*
* Track volumes by month
*
*
* */
    public void initConsole() throws Exception {

        Scanner in = new Scanner(System.in);
        int mainMenuNumber = 999;

        while (mainMenuNumber != 0) {
            System.out.println("\nPlease select an action");
            System.out.println("1). Daily ODL functions");
            System.out.println("2). Printout Reddit template");
            System.out.println("3). Display ODL totals");
            System.out.println("4). Display YTD stats");
            System.out.println("5). Display Forecasting");
            System.out.println("0). Exit");
            mainMenuNumber = in.nextInt();

            mainMenuRouter(mainMenuNumber);
        }
    }

    public void mainMenuRouter(int s) throws Exception {

        if(s == 0) {
            System.exit(0);
        } else if (s == 1) {

            int dailyODLFunctionNumber = 999;
            while (dailyODLFunctionNumber != 0) {

                Scanner in = new Scanner(System.in);
                System.out.println("\nDaily ODL Functions");
                System.out.println("\n1). Display individual days stats");
                System.out.println("2). Display all ODL volumes");
                System.out.println("3). Get weekly ODL data");
                System.out.println("4). Get weekly ODL data - verbose");
                System.out.println("5). Rank days by volume");
                System.out.println("0). Exit");
                dailyODLFunctionNumber = in.nextInt();
                dailyODLFunctionsRouter(dailyODLFunctionNumber);
            }
        } else if (s == 2) {

            System.out.println("Under Construction");
            System.out.println("Reddit Template\n");
            //Have a toggle to add new ATH text at the end
            System.out.println("ODL posted a 7 day total of " + "placeholder");
            System.out.println("This was a week over week increase of " + "placeholder");
            System.out.println("The largest day was " + "placeholder" + " which posted " + "placeholder");
            //Implement leaderboard logic here
            System.out.println("Previous day placeholder" + " takse the " + " #placeholder" + " spot on the lader-boards out of " + "number of entries placeholder");
            System.out.println("End of Year ODL volumes forecast is between " + "forecast low placeholder" + " and " + "forecase high placeholder");
            System.out.println("ODL was " + "percentage placeholder" + " of reported ledger volume");
            //Implement formatting for corridor tables
            System.out.println("Corridor table information");
        } else if (s == 3) {

            JSONObject statsObject = UtilityScan.getStats();

            //Begin total recorded ODL
            String dailyAth = (String) statsObject.get("totalVolume").toString();
            int val = new BigDecimal(dailyAth).intValue();
            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);
            Money money = Money.parse("USD " + val);
            System.out.println("Total recorded ODL is: " + formatter.print(money));

            JSONObject athsPerCorridor = (JSONObject) statsObject.get("athsPerCorridor");

            //Begin corridor totals
            System.out.println("\nCorridor totals: ");

            money = Money.parse("USD 0.00");
            CurrencyUnit usd = CurrencyUnit.of("USD");
            JSONObject dailyVolumeObject = (JSONObject) statsObject.get("volumePerCorridor");
            Map<String, Double> totalSumByCorridor = new HashMap<>();

            Iterator<String> dateKeys = dailyVolumeObject.keySet().iterator();
            while(dateKeys.hasNext()) {

                String dateKey = dateKeys.next();
                JSONObject corridorObject = (JSONObject) dailyVolumeObject.get(dateKey);

                Iterator<String> corridorKeys = corridorObject.keySet().iterator();
                while(corridorKeys.hasNext()) {

                    String corridorKey = corridorKeys.next();
                    try {
                        totalSumByCorridor.replace(corridorKey, (Double) corridorObject.get(corridorKey) + totalSumByCorridor.get(corridorKey));
                    } catch (NullPointerException e) {
                        totalSumByCorridor.put(corridorKey, (Double) corridorObject.get(corridorKey));
                    }
                }
            }
            for (Map.Entry<String, Double> entry : totalSumByCorridor.entrySet()) {

                Double value = Math.round(entry.getValue() * 100.00) / 100.00;
                money = Money.of(usd, value);
                System.out.println(entry.getKey() + " - " + formatter.print(money) );
            }

            //Begin corridor ATHs
            System.out.println("\nATH per corridor: ");

            money = Money.parse("USD 0.00");
            JSONObject corridorObject = (JSONObject) statsObject.get("athPerCorridor");
            Iterator<String> keys = corridorObject.keySet().iterator();
            while(keys.hasNext()) {

                String key = keys.next();
                Double value = (Double) corridorObject.get(key);
                value = Math.round(value * 100.00) / 100.00;
                money = Money.of(usd, value);
                System.out.println(key + " - " + formatter.print(money));
            }
        } else if (s == 4) {

            System.out.println("Under Construction");
            System.out.println("YTD recorded ODL is: ");
            System.out.println("YTD Corridor totals: ");

            JSONObject statsObject = UtilityScan.getStats();

            Money money = Money.parse("USD 0.00");
            CurrencyUnit usd = CurrencyUnit.of("USD");
            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);
            JSONObject dailyVolumeObject = (JSONObject) statsObject.get("volumePerCorridor");
            Map<String, Double> totalSumByCorridor = new HashMap<>();

            Iterator<String> dateKeys = dailyVolumeObject.keySet().iterator();
            while(dateKeys.hasNext()) {

                String dateKey = dateKeys.next();
                if (Pattern.matches("20-\\d\\d-\\d\\d",dateKey )){

                    JSONObject corridorObject = (JSONObject) dailyVolumeObject.get(dateKey);

                    Iterator<String> corridorKeys = corridorObject.keySet().iterator();
                    while(corridorKeys.hasNext()) {

                        String corridorKey = corridorKeys.next();
                        try {
                            totalSumByCorridor.replace(corridorKey, (Double) corridorObject.get(corridorKey) + totalSumByCorridor.get(corridorKey));
                        } catch (NullPointerException e) {
                            totalSumByCorridor.put(corridorKey, (Double) corridorObject.get(corridorKey));
                        }
                    }
                }

            }
            for (Map.Entry<String, Double> entry : totalSumByCorridor.entrySet()) {

                Double value = Math.round(entry.getValue() * 100.00) / 100.00;
                money = Money.of(usd, value);
                System.out.println(entry.getKey() + " - " + formatter.print(money) );
            }

            System.out.println("\nYTD ATH per corridor: ");
        } else if (s == 5) {

            System.out.println("Under Construction");
            System.out.println("2020 forecast based on past 7 days and no future growth");
            System.out.println("2020 forecast based on past 7 days and " + "growth placeholder");
            System.out.println("2020 forecast based on daily average and no future growth");
            System.out.println("2020 forecast based on daily average and " + "growth placeholder");
            System.out.println("2020 forecast based on YTD average growth");
        }
    }

    public void dailyODLFunctionsRouter(int s) throws Exception {

        if(s == 0) {

        } else if (s == 1) {

            // Take input for what day is desired
            boolean isDateFormatGood = false;
            String dateInput = "";
            while (!isDateFormatGood) {

                Scanner consoleInput = new Scanner(System.in);
                System.out.println("\nInput desired ODL day (YY-MM-DD)");
                dateInput = consoleInput.next();
                isDateFormatGood = DateFormatting.checkFormat(dateInput);
                if (!isDateFormatGood) {
                    System.out.println("Please print the date in the following format: YY-MM-DD");
                }
            }

            // Get stats from utility-scan
            JSONObject statsObject = UtilityScan.getStats();

            // Print the days total
            JSONObject dayObject = UtilityScan.getDay(statsObject, dateInput);
            Money daysTotal = UtilityScan.sumCorridors(dayObject);

            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);

            SimpleDateFormat stringToDate = new SimpleDateFormat("yy-MM-dd");
            Date date1 = stringToDate.parse(dateInput);

            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
            System.out.println("\nFor the day " + dateInput + " (" + dayOfWeekFormat.format(date1) + ")");
            System.out.println("Total for the day was: " + formatter.print(daysTotal));

            // Display each corridor formatted
            UtilityScan.printFormattedCorridors(dayObject);

        } else if (s == 2) {

            // Get stats from utility-scan
            JSONObject statsObject = UtilityScan.getStats();

            // Loop through all days and print the daily total
            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);

            SimpleDateFormat startDateFormat = new SimpleDateFormat("yy-MM-dd");
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
            DateTimeFormatter currentDateFormat = DateTimeFormatter.ofPattern("yy-MM-dd");

            String startDate = "19-10-26";
            Calendar c = Calendar.getInstance();

            LocalDate now = LocalDate.now().plusDays(1);
            String currentDate = currentDateFormat.format(now);

            c.setTime(startDateFormat.parse(startDate));

            while (!currentDate.equals(startDateFormat.format(c.getTime()))) {

                JSONObject dayObject = UtilityScan.getDay(statsObject, startDateFormat.format(c.getTime()));
                Money daysTotal = UtilityScan.sumCorridors(dayObject);

                System.out.println(startDateFormat.format(c.getTime()) + ": " +
                        dayOfWeekFormat.format(c.getTime()) + " - " +
                        formatter.print(daysTotal));

                c.add(Calendar.DATE, 1);
            }
        } else if (s == 3) {

            // Get stats from utility-scan
            JSONObject statsObject = UtilityScan.getStats();

            //Loop through all days and print 7 day totals
            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);

            Money lastWeeksTotal = Money.parse("USD 1.00");

            SimpleDateFormat startDateFormat = new SimpleDateFormat("yy-MM-dd");
            DateTimeFormatter currentDateFormat = DateTimeFormatter.ofPattern("yy-MM-dd");

            String startDate = "20-01-01";
            Calendar c = Calendar.getInstance();

            LocalDate now = LocalDate.now().plusDays(1);
            String currentDate = currentDateFormat.format(now);


            c.setTime(startDateFormat.parse(startDate));
            while (!currentDate.equals(startDateFormat.format(c.getTime()))) {

                int countDays = 0;
                Money weeksTotal = Money.parse("USD 0.00");
                while(countDays != 7 && !currentDate.equals(startDateFormat.format(c.getTime()))) {

                    JSONObject dayObject = UtilityScan.getDay(statsObject, startDateFormat.format(c.getTime()));
                    weeksTotal = weeksTotal.plus(UtilityScan.sumCorridors(dayObject));
                    countDays++;
                    if (countDays != 7) {
                        c.add(Calendar.DATE, 1);
                    }
                }

                System.out.println(startDateFormat.format(c.getTime()) + ": " +
                        formatter.print(weeksTotal) + " a " +
                        new BigDecimal(String.valueOf(weeksTotal.getAmount()))
                                .divide(new BigDecimal(String.valueOf(lastWeeksTotal.getAmount())), 4,RoundingMode.DOWN)
                                .subtract(new BigDecimal("1"))
                                .multiply(new BigDecimal("100")) + "%" +
                        " change over last week");
                lastWeeksTotal = weeksTotal;
                if (currentDate.equals(startDateFormat.format(c.getTime()))) {
                    break;
                }
                c.add(Calendar.DATE, 1);
            }

        } else if (s == 4) {

            // Get stats from utility-scan
            JSONObject statsObject = UtilityScan.getStats();

            //Loop through all days and print 7 day totals
            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);

            Money lastWeeksTotal = Money.parse("USD 1.00");

            SimpleDateFormat startDateFormat = new SimpleDateFormat("yy-MM-dd");
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
            DateTimeFormatter currentDateFormat = DateTimeFormatter.ofPattern("yy-MM-dd");

            String startDate = "20-01-01";
            Calendar c = Calendar.getInstance();

            LocalDate now = LocalDate.now().plusDays(1);
            String currentDate = currentDateFormat.format(now);

            c.setTime(startDateFormat.parse(startDate));
            while (!currentDate.equals(startDateFormat.format(c.getTime()))) {

                int countDays = 0;
                Money weeksTotal = Money.parse("USD 0.00");
                while(countDays != 7 && !currentDate.equals(startDateFormat.format(c.getTime()))) {

                    JSONObject dayObject = UtilityScan.getDay(statsObject, startDateFormat.format(c.getTime()));
                    System.out.println(startDateFormat.format(c.getTime()) + " - " +
                            dayOfWeekFormat.format(c.getTime()) + ": " +
                            formatter.print(UtilityScan.sumCorridors(dayObject)));
                    weeksTotal = weeksTotal.plus(UtilityScan.sumCorridors(dayObject));
                    countDays++;
                    if (countDays != 7) {
                        c.add(Calendar.DATE, 1);
                    }
                }

                System.out.println(startDateFormat.format(c.getTime()) + ": " +
                        formatter.print(weeksTotal) + " a " +
                        new BigDecimal(String.valueOf(weeksTotal.getAmount()))
                                .divide(new BigDecimal(String.valueOf(lastWeeksTotal.getAmount())), 4,RoundingMode.DOWN)
                                .subtract(new BigDecimal("1"))
                                .multiply(new BigDecimal("100")) + "%" +
                        " change over last week");
                lastWeeksTotal = weeksTotal;
                if (currentDate.equals(startDateFormat.format(c.getTime()))) {
                    break;
                }
                c.add(Calendar.DATE, 1);
            }
        } else if (s == 5) {
            System.out.println("Under Construction");
        }
    }
}
