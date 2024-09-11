package com.chuangyao.niu.entity;

//以下是一种将纸飞机机器人的 API 接口独立出来的方式：
//首先，创建一个专门的 API 类来封装对PaperPlaneAccountingBot的操作：

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class PaperPlaneAccountingApi {
    private PaperPlaneAccountingBot bot;

    public PaperPlaneAccountingApi() {
        bot = new PaperPlaneAccountingBot();
    }

    public void setOperator(String operator) {
        bot.setOperator(operator);
    }

    public void deleteOperator() {
        bot.setOperator(null);
    }

    public void setFeeRate(double feeRate) {
        bot.setFeeRate(feeRate);
    }

    public void setExchangeRate(double exchangeRate) {
        bot.setExchangeRate(exchangeRate);
    }

    public void setUsdExchangeRate(double usdExchangeRate) {
        bot.setUsdExchangeRate(usdExchangeRate);
    }

    public void setRealTimeRate() {
        bot.setRealTimeRate();
    }

    public void startRecording() {
        bot.startRecording();
    }

    public void addTransaction(String type, double amount, String currency, String note) {
        bot.addTransaction(type, amount, currency, note);
    }

    public void showTodayBill() {
        bot.showTodayBill();
    }

    public void showHistoricalBill() {
        bot.showHistoricalBill();
    }

    public void deleteTodayBill() {
        bot.deleteTodayBill();
    }

    public void deleteHistoricalBill() {
        bot.deleteHistoricalBill();
    }

    public void saveBillToFile() {
        bot.saveBillToFile();
    }

    public double getRealTimeExchangeRate() {
        return bot.getRealTimeExchangeRate();
    }

    public void addTagToTransaction(int transactionIndex, String tag) {
        bot.addTagToTransaction(transactionIndex, tag);
    }

    public void searchByTag(String tag) {
        bot.searchByTag(tag);
    }
}

public class AAMain {
    public static void main(String[] args) {
        PaperPlaneAccountingApi api = new PaperPlaneAccountingApi();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command: ");
            String commandInput = scanner.nextLine();

            try {
                // 根据输入命令调用相应的 API 方法
                if (commandInput.startsWith("设置操作人 @")) {
                    api.setOperator(commandInput.substring(6));
                } else if (commandInput.startsWith("删除操作人 @")) {
                    api.deleteOperator();
                } else if (commandInput.startsWith("设置费率")) {
                    double feeRate = Double.parseDouble(commandInput.substring(4).replace("%", "")) / 100;
                    api.setFeeRate(feeRate);
                } else if (commandInput.startsWith("设置汇率")) {
                    double exchangeRate = Double.parseDouble(commandInput.substring(4));
                    api.setExchangeRate(exchangeRate);
                } else if (commandInput.startsWith("设置美元汇率")) {
                    double usdExchangeRate = Double.parseDouble(commandInput.substring(6));
                    api.setUsdExchangeRate(usdExchangeRate);
                } else if (commandInput.equals("设置实时汇率")) {
                    api.setRealTimeRate();
                } else if (commandInput.equals("开始记账")) {
                    api.startRecording();
                } else if (commandInput.startsWith("入款+")) {
                    double amount = Double.parseDouble(commandInput.substring(4));
                    api.addTransaction("Deposit", amount, "Unknown", "Today");
                } else if (commandInput.startsWith("入款-")) {
                    double amount = Double.parseDouble(commandInput.substring(4));
                    api.addTransaction("Withdrawal", amount, "Unknown", "Today");
                } else if (commandInput.equals("显示账单(+0)")) {
                    api.showTodayBill();
                } else if (commandInput.equals("显示历史账单")) {
                    api.showHistoricalBill();
                } else if (commandInput.equals("显示实时汇率（z0、Z0）")) {
                    if (api.getRealTimeExchangeRate() > 0) {
                        System.out.println("Real-time exchange rate: " + api.getRealTimeExchangeRate());
                    } else {
                        System.out.println("Not using real-time rate.");
                    }
                } else if (commandInput.equals("删除账单")) {
                    api.deleteTodayBill();
                } else if (commandInput.equals("删除历史账单")) {
                    api.deleteHistoricalBill();
                } else if (commandInput.equals("保存账单")) {
                    api.saveBillToFile();
                } else if (commandInput.startsWith("添加标签 ")) {
                    String[] parts = commandInput.split(" ");
                    int transactionIndex = Integer.parseInt(parts[1]);
                    String tag = parts[2];
                    api.addTagToTransaction(transactionIndex, tag);
                } else if (commandInput.startsWith("搜索标签 ")) {
                    String tag = commandInput.substring(5);
                    api.searchByTag(tag);
                } else {
                    System.out.println("Invalid command.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please check your input.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}


class Transaction {
    String type;
    double amount;
    String currency;
    String note;
    Date transactionDate;

    public Transaction(String type, double amount, String currency, String note, Date transactionDate) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.note = note;
        this.transactionDate = transactionDate;
    }
}

enum CommandType {
    SET_OPERATOR,
    DELETE_OPERATOR,
    SET_ALL_USERS,
    SHOW_OPERATOR,
    SHOW_BILL_TODAY,
    SHOW_HISTORICAL_BILL,
    SHOW_REAL_TIME_RATE,
    DELETE_BILL_TODAY,
    DELETE_HISTORICAL_BILL,
    SAVE_BILL,
    SET_FEE_RATE,
    SET_EXCHANGE_RATE,
    SET_USD_EXCHANGE_RATE,
    SET_REAL_TIME_RATE,
    START_RECORDING,
    DEPOSIT_PLUS,
    DEPOSIT_MINUS,
    ADJUST_PLUS,
    ADJUST_MINUS,
    DISBURSEMENT,
    DISBURSEMENT_REVERSAL,
    DISBURSEMENT_U,
    DISBURSEMENT_REVERSAL_U,
    DISBURSEMENT_USD,
    DISBURSEMENT_REVERSAL_USD,
    SHOW_MODE_1,
    SHOW_MODE_2,
    SHOW_MODE_3,
    ADD_TAG_TO_TRANSACTION,
    SEARCH_BY_TAG,
    INVALID_COMMAND;
}

class PaperPlaneAccountingBot {
    String operator;
    double feeRate;
    double exchangeRate;
    double usdExchangeRate;
    boolean isRealTimeRate;
    List<Transaction> transactions;
    boolean isRecording;

    public PaperPlaneAccountingBot() {
        transactions = new ArrayList<>();
        isRecording = false;
    }

    public void setOperator(String operator) {
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be empty or null.");
        }
        this.operator = operator;
    }

    public void setFeeRate(double feeRate) {
        if (feeRate < 0) {
            throw new IllegalArgumentException("Fee rate cannot be negative.");
        }
        this.feeRate = feeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        if (exchangeRate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive.");
        }
        this.exchangeRate = exchangeRate;
    }

    public void setUsdExchangeRate(double usdExchangeRate) {
        if (usdExchangeRate <= 0) {
            throw new IllegalArgumentException("USD exchange rate must be positive.");
        }
        this.usdExchangeRate = usdExchangeRate;
    }

    public void setRealTimeRate() {
        isRealTimeRate = true;
    }

    public void startRecording() {
        isRecording = true;
    }

    public void addTransaction(String type, double amount, String currency, String note) {
        addTransaction(type, amount, currency, note, new Date());
    }

    public void addTransaction(String type, double amount, String currency, String note, Date transactionDate) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be empty.");
        }
        transactions.add(new Transaction(type, amount, currency, note, transactionDate));
    }

    public void showTodayBill() {
        System.out.println("Today's Transactions:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        for (Transaction transaction : transactions) {
            if (dateFormat.format(transaction.transactionDate).equals(today)) {
                printTransaction(transaction);
            }
        }
    }

    public void showHistoricalBill() {
        System.out.println("Historical Transactions:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        for (Transaction transaction : transactions) {
            if (!dateFormat.format(transaction.transactionDate).equals(today)) {
                printTransaction(transaction);
            }
        }
    }

    private void printTransaction(Transaction transaction) {
        SimpleDateFormat detailedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(transaction.type + " " + transaction.amount + " " + transaction.currency + " " + transaction.note + " " + detailedDateFormat.format(transaction.transactionDate));
    }

    public void deleteTodayBill() {
        List<Transaction> newTransactions = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        for (Transaction transaction : transactions) {
            if (!dateFormat.format(transaction.transactionDate).equals(today)) {
                newTransactions.add(transaction);
            }
        }
        transactions = newTransactions;
    }

    public void deleteHistoricalBill() {
        transactions.clear();
    }

    public void saveBillToFile() {
        try {
            FileWriter writer = new FileWriter("transactions.txt");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Transaction transaction : transactions) {
                writer.write(transaction.type + "," + transaction.amount + "," + transaction.currency + "," + transaction.note + "," + dateFormat.format(transaction.transactionDate) + "\n");
            }
            writer.close();
            System.out.println("Bill saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving bill to file: " + e.getMessage());
        }
    }

    // Simulate getting real-time exchange rate (replace with actual implementation)
    public double getRealTimeExchangeRate() {
        // For demonstration purposes, return a random value between 6 and 7.
        return Math.random() + 6;
    }

    public void addTagToTransaction(int transactionIndex, String tag) {
        if (transactionIndex < 0 || transactionIndex >= transactions.size()) {
            throw new IllegalArgumentException("Invalid transaction index.");
        }
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be empty.");
        }
        Transaction transaction = transactions.get(transactionIndex);
        transaction.note += " [Tag: " + tag + "]";
    }

    public void searchByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be empty.");
        }
        System.out.println("Transactions with tag '" + tag + "':");
        for (Transaction transaction : transactions) {
            if (transaction.note.contains(" [Tag: " + tag + "]")) {
                printTransaction(transaction);
            }
        }
    }

    // Method to calculate based on current settings
    public double calculateWithFeeAndExchange(double amount, String currency) {
        if (currency.equals("U")) {
            if (feeRate >= 0) {
                return amount * (1 - feeRate) / exchangeRate;
            } else {
                return amount * (1 + Math.abs(feeRate)) / exchangeRate;
            }
        } else if (currency.equals("美")) {
            if (feeRate >= 0) {
                return amount * (1 - feeRate) / usdExchangeRate;
            } else {
                return amount * (1 + Math.abs(feeRate)) / usdExchangeRate;
            }
        } else {
            return amount;
        }
    }
}

class CommandParser {
    public static CommandType parseCommand(String commandInput) {
        if (commandInput.startsWith("设置操作人 @")) {
            return CommandType.SET_OPERATOR;
        } else if (commandInput.startsWith("删除操作人 @")) {
            return CommandType.DELETE_OPERATOR;
        } else if (commandInput.startsWith("设置所有人")) {
            return CommandType.SET_ALL_USERS;
        } else if (commandInput.startsWith("显示操作人")) {
            return CommandType.SHOW_OPERATOR;
        } else if (commandInput.equals("显示账单(+0)")) {
            return CommandType.SHOW_BILL_TODAY;
        } else if (commandInput.equals("显示历史账单")) {
            return CommandType.SHOW_HISTORICAL_BILL;
        } else if (commandInput.equals("显示实时汇率（z0、Z0）")) {
            return CommandType.SHOW_REAL_TIME_RATE;
        } else if (commandInput.equals("删除账单")) {
            return CommandType.DELETE_BILL_TODAY;
        } else if (commandInput.equals("删除历史账单")) {
            return CommandType.DELETE_HISTORICAL_BILL;
        } else if (commandInput.equals("保存账单")) {
            return CommandType.SAVE_BILL;
        } else if (commandInput.startsWith("设置费率")) {
            return CommandType.SET_FEE_RATE;
        } else if (commandInput.startsWith("设置汇率")) {
            return CommandType.SET_EXCHANGE_RATE;
        } else if (commandInput.startsWith("设置美元汇率")) {
            return CommandType.SET_USD_EXCHANGE_RATE;
        } else if (commandInput.equals("设置实时汇率")) {
            return CommandType.SET_REAL_TIME_RATE;
        } else if (commandInput.equals("开始记账")) {
            return CommandType.START_RECORDING;
        } else if (commandInput.startsWith("入款+")) {
            return CommandType.DEPOSIT_PLUS;
        } else if (commandInput.startsWith("入款-")) {
            return CommandType.DEPOSIT_MINUS;
        } else if (commandInput.startsWith("+")) {
            return CommandType.ADJUST_PLUS;
        } else if (commandInput.startsWith("-")) {
            return CommandType.ADJUST_MINUS;
        } else if (commandInput.startsWith("下发")) {
            return CommandType.DISBURSEMENT;
        } else if (commandInput.startsWith("下发-")) {
            return CommandType.DISBURSEMENT_REVERSAL;
        } else if (commandInput.startsWith("下发xxxU") || commandInput.startsWith("下发-xxxU")) {
            return CommandType.DISBURSEMENT_U;
        } else if (commandInput.startsWith("下发xxx美") || commandInput.startsWith("下发-xxx美")) {
            return CommandType.DISBURSEMENT_USD;
        } else if (commandInput.equals("显示模式1")) {
            return CommandType.SHOW_MODE_1;
        } else if (commandInput.equals("显示模式2")) {
            return CommandType.SHOW_MODE_2;
        } else if (commandInput.equals("显示模式3")) {
            return CommandType.SHOW_MODE_3;
        } else if (commandInput.startsWith("添加标签 ")) {
            return CommandType.ADD_TAG_TO_TRANSACTION;
        } else if (commandInput.startsWith("搜索标签 ")) {
            return CommandType.SEARCH_BY_TAG;
        } else {
            return CommandType.INVALID_COMMAND;
        }
    }
}
