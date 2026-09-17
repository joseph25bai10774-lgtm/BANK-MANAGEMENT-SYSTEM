import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BankManagementSystem {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
// =====================================================================
//  MODEL
// =====================================================================
class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter TS_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final long accountNumber;
    private String name;
    private String dob;
    private String mobile;
    private String pin;
    private double balance;
    private final List<String> transactions = new ArrayList<>();
    public Account(long accountNumber, String name, String dob, String mobile,
                    String pin, double openingBalance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.dob = dob;
        this.mobile = mobile;
        this.pin = pin;
        this.balance = openingBalance;
        log("ACCOUNT OPENED", openingBalance);
    }
    public void deposit(double amount) {
        balance += amount;
        log("DEPOSIT", amount);
    }
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        log("WITHDRAW", amount);
        return true;
    }
    public boolean changePin(String oldPin, String newPin) {
        if (!this.pin.equals(oldPin)) {
            return false;
        }
        this.pin = newPin;
        log("PIN CHANGED", 0);
        return true;
    }
    public boolean verifyPin(String candidate) {
        return this.pin.equals(candidate);
    }
    private void log(String type, double amount) {
        String line = String.format("[%s] %-14s  Amount: %10s  Balance: %10.2f",
                LocalDateTime.now().format(TS_FORMAT),
                type,
                amount == 0 ? "-" : String.format("%.2f", amount),
                balance);
        transactions.add(line);
    }
    public long getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public double getBalance() { return balance; }
    public List<String> getTransactions() { return transactions; }
}
// =====================================================================
//  PERSISTENCE
// =====================================================================

final class BankDataStore {
    private static final String DATA_FILE = "bank_data.ser";
    private static BankDataStore instance;
    private Map<Long, Account> accounts = new HashMap<>();
    private long nextAccountNumber = 100100;
    private BankDataStore() {
        load();
    }
    public static synchronized BankDataStore getInstance() {
        if (instance == null) {
            instance = new BankDataStore();
        }
        return instance;
    }
    @SuppressWarnings("unchecked")
    private void load() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            accounts = (Map<Long, Account>) in.readObject();
            nextAccountNumber = in.readLong();
        } catch (Exception e) {
            System.err.println("Could not load existing data, starting fresh: " + e.getMessage());
            accounts = new HashMap<>();
        }
    }
    public synchronized void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(accounts);
            out.writeLong(nextAccountNumber);
        } catch (IOException e) {
            System.err.println("Failed to save bank data: " + e.getMessage());
        }
    }
    public synchronized Account createAccount(String name, String dob, String mobile,
                                               String pin, double openingBalance) {
        long accNo = nextAccountNumber++;
        Account acc = new Account(accNo, name, dob, mobile, pin, openingBalance);
        accounts.put(accNo, acc);
        save();
        return acc;
    }
    public Account findAccount(long accountNumber) {
        return accounts.get(accountNumber);
    }
    public boolean accountExists(long accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}
// =====================================================================
//  SHARED UI THEME / HELPERS
// =====================================================================
final class UITheme {
    public static final Color PRIMARY = new Color(13, 59, 102);
    public static final Color PRIMARY_DARK = new Color(8, 40, 72);
    public static final Color ACCENT = new Color(212, 160, 23);
    public static final Color BG = new Color(240, 244, 248);
    public static final Color DANGER = new Color(176, 42, 42);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private UITheme() {}
    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, PRIMARY, Color.WHITE);
        return b;
    }
    public static JButton accentButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, ACCENT, Color.WHITE);
        return b;
    }
    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, DANGER, Color.WHITE);
        return b;
    }
    private static void styleButton(JButton b, Color bg, Color fg) {
        b.setFont(BUTTON_FONT);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorderPainted(false);
    }
    public static JLabel headerBar(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(PRIMARY);
        label.setForeground(Color.WHITE);
        label.setFont(TITLE_FONT);
        label.setBorder(new EmptyBorder(18, 10, 18, 10));
        return label;
    }
    public static JTextField textField() {
        JTextField t = new JTextField();
        t.setFont(LABEL_FONT);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 210)),
                new EmptyBorder(6, 8, 6, 8)));
        return t;
    }
    public static JPasswordField passwordField() {
        JPasswordField t = new JPasswordField();
        t.setFont(LABEL_FONT);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 210)),
                new EmptyBorder(6, 8, 6, 8)));
        return t;
    }
    public static JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(LABEL_FONT);
        return l;
    }
    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public static boolean confirm(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Please Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
// =====================================================================
//  LOGIN SCREEN
// =====================================================================
class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Bank Management System - Login");
        setSize(480, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        root.add(UITheme.headerBar("\uD83C\uDFE6 National Bank"), BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.BG);
        form.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 0, 8, 0);
        JLabel sub = new JLabel("Sign in to your account", SwingConstants.CENTER);
        sub.setFont(UITheme.HEADER_FONT);
        sub.setForeground(UITheme.PRIMARY_DARK);
        c.gridy = 0;
        form.add(sub, c);
        c.gridy = 1;
        form.add(UITheme.formLabel("Account Number"), c);
        JTextField accField = UITheme.textField();
        c.gridy = 2;
        form.add(accField, c);
        c.gridy = 3;
        form.add(UITheme.formLabel("PIN"), c);
        JPasswordField pinField = UITheme.passwordField();
        c.gridy = 4;
        form.add(pinField, c);
        JButton loginBtn = UITheme.primaryButton("Login");
        c.gridy = 5;
        c.insets = new Insets(20, 0, 8, 0);
        form.add(loginBtn, c);
        JButton signupBtn = UITheme.accentButton("New here? Create an Account");
        c.gridy = 6;
        c.insets = new Insets(4, 0, 8, 0);
        form.add(signupBtn, c);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
        loginBtn.addActionListener(e -> attemptLogin(accField, pinField));
        pinField.addActionListener(e -> attemptLogin(accField, pinField));
        signupBtn.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });
    }
    private void attemptLogin(JTextField accField, JPasswordField pinField) {
        String accText = accField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        if (accText.isEmpty() || pin.isEmpty()) {
            UITheme.error(this, "Please enter both account number and PIN.");
            return;
        }
        long accNo;
        try {
            accNo = Long.parseLong(accText);
        } catch (NumberFormatException ex) {
            UITheme.error(this, "Account number must be numeric.");
            return;
        }
        Account acc = BankDataStore.getInstance().findAccount(accNo);
        if (acc == null) {
            UITheme.error(this, "No account found with that number.");
            return;
        }
        if (!acc.verifyPin(pin)) {
            UITheme.error(this, "Incorrect PIN.");
            return;
        }

        new DashboardFrame(acc).setVisible(true);
        dispose();
    }
    public static void reopen() {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
// =====================================================================
//  SIGNUP SCREEN
// =====================================================================
class SignupFrame extends JFrame {
    public SignupFrame() {
        setTitle("Bank Management System - Open New Account");
        setSize(500, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        root.add(UITheme.headerBar("\uD83C\uDFE6  Open a New Account"), BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        int row = 0;
        JTextField nameField = UITheme.textField();
        JTextField dobField = UITheme.textField();
        dobField.setToolTipText("Format: DD-MM-YYYY");
        JTextField mobileField = UITheme.textField();
        JTextField depositField = UITheme.textField();
        JPasswordField pinField = UITheme.passwordField();
        JPasswordField confirmPinField = UITheme.passwordField();
        row = addField(form, c, row, "Full Name", nameField);
        row = addField(form, c, row, "Date of Birth (DD-MM-YYYY)", dobField);
        row = addField(form, c, row, "Mobile Number", mobileField);
        row = addField(form, c, row, "Opening Deposit Amount", depositField);
        row = addField(form, c, row, "Create 4-digit PIN", pinField);
        row = addField(form, c, row, "Confirm PIN", confirmPinField);
        JButton createBtn = UITheme.primaryButton("Create Account");
        c.gridy = row++;
        c.insets = new Insets(20, 0, 8, 0);
        form.add(createBtn, c);
        JButton backBtn = UITheme.accentButton("Back to Login");
        c.gridy = row;
        c.insets = new Insets(4, 0, 8, 0);
        form.add(backBtn, c);
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);
        setContentPane(root);
        createBtn.addActionListener(e -> createAccount(
                nameField, dobField, mobileField, depositField, pinField, confirmPinField));
        backBtn.addActionListener(e -> {
            LoginFrame.reopen();
            dispose();
        });
    }
    private int addField(JPanel form, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridy = row++;
        form.add(UITheme.formLabel(label), c);
        c.gridy = row++;
        form.add(field, c);
        return row;
    }
    private void createAccount(JTextField nameField, JTextField dobField, JTextField mobileField,
                                JTextField depositField, JPasswordField pinField,
                                JPasswordField confirmPinField) {

        String name = nameField.getText().trim();
        String dob = dobField.getText().trim();
        String mobile = mobileField.getText().trim();
        String depositText = depositField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();
        if (name.isEmpty() || dob.isEmpty() || mobile.isEmpty() || depositText.isEmpty()
                || pin.isEmpty() || confirmPin.isEmpty()) {
            UITheme.error(this, "All fields are required.");
            return;
        }
        if (!mobile.matches("\\d{10}")) {
            UITheme.error(this, "Mobile number must be exactly 10 digits.");
            return;
        }
        if (!pin.matches("\\d{4}")) {
            UITheme.error(this, "PIN must be exactly 4 digits.");
            return;
        }
        if (!pin.equals(confirmPin)) {
            UITheme.error(this, "PIN and Confirm PIN do not match.");
            return;
        }
        double openingDeposit;
        try {
            openingDeposit = Double.parseDouble(depositText);
        } catch (NumberFormatException ex) {
            UITheme.error(this, "Opening deposit must be a valid number.");
            return;
        }
        if (openingDeposit < 500) {
            UITheme.error(this, "Minimum opening deposit is 500.00");
            return;
        }
        Account acc = BankDataStore.getInstance()
                .createAccount(name, dob, mobile, pin, openingDeposit);

        UITheme.info(this, "Account created successfully!\n\n"
                + "Your Account Number is: " + acc.getAccountNumber()
                + "\n\nPlease note this down. You'll need it to log in.");

        LoginFrame.reopen();
        dispose();
    }
}
// =====================================================================
//  DASHBOARD (main menu after login)
// =====================================================================
class DashboardFrame extends JFrame {
    private final Account account;
    private JLabel balanceLabel;
    public DashboardFrame(Account account) {
        this.account = account;

        setTitle("Bank Management System - Dashboard");
        setSize(560, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        root.add(UITheme.headerBar("\uD83C\uDFE6  Welcome, " + account.getName()), BorderLayout.NORTH);
        JPanel top = new JPanel();
        top.setBackground(UITheme.PRIMARY_DARK);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel accLabel = new JLabel("Account No: " + account.getAccountNumber());
        accLabel.setForeground(Color.WHITE);
        accLabel.setFont(UITheme.LABEL_FONT);
        accLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceLabel = new JLabel();
        balanceLabel.setForeground(UITheme.ACCENT);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshBalance();
        top.add(accLabel);
        top.add(Box.createVerticalStrut(6));
        top.add(balanceLabel);
        JPanel grid = new JPanel(new GridLayout(4, 2, 16, 16));
        grid.setBackground(UITheme.BG);
        grid.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));
        JButton depositBtn = UITheme.primaryButton("Deposit");
        JButton withdrawBtn = UITheme.primaryButton("Withdraw");
        JButton fastCashBtn = UITheme.primaryButton("Fast Cash");
        JButton balanceBtn = UITheme.primaryButton("Balance Enquiry");
        JButton statementBtn = UITheme.primaryButton("Mini Statement");
        JButton pinBtn = UITheme.primaryButton("Change PIN");
        JButton logoutBtn = UITheme.accentButton("Logout");
        JButton exitBtn = UITheme.dangerButton("Exit");
        grid.add(depositBtn);
        grid.add(withdrawBtn);
        grid.add(fastCashBtn);
        grid.add(balanceBtn);
        grid.add(statementBtn);
        grid.add(pinBtn);
        grid.add(logoutBtn);
        grid.add(exitBtn);
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.BG);
        center.add(top, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
        depositBtn.addActionListener(e -> new DepositDialog(this, account, this::refreshBalance).setVisible(true));
        withdrawBtn.addActionListener(e -> new WithdrawDialog(this, account, this::refreshBalance).setVisible(true));
        fastCashBtn.addActionListener(e -> new FastCashDialog(this, account, this::refreshBalance).setVisible(true));
        balanceBtn.addActionListener(e -> UITheme.info(this,
                "Account Holder : " + account.getName()
                + "\nAccount Number : " + account.getAccountNumber()
                + "\nCurrent Balance : " + String.format("%.2f", account.getBalance())));
        statementBtn.addActionListener(e -> new MiniStatementDialog(this, account).setVisible(true));
        pinBtn.addActionListener(e -> new ChangePinDialog(this, account).setVisible(true));
        logoutBtn.addActionListener(e -> {
            if (UITheme.confirm(this, "Are you sure you want to logout?")) {
                LoginFrame.reopen();
                dispose();
            }
        });
        exitBtn.addActionListener(e -> {
            if (UITheme.confirm(this, "Are you sure you want to exit?")) {
                System.exit(0);
            }
        });
    }
    public void refreshBalance() {
        balanceLabel.setText("Balance: " + String.format("%.2f", account.getBalance()));
    }
}
// =====================================================================
//  DEPOSIT
// =====================================================================
class DepositDialog extends JDialog {
    public DepositDialog(JFrame parent, Account account, Runnable onSuccess) {
        super(parent, "Deposit", true);
        setSize(360, 220);
        setLocationRelativeTo(parent);
        setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        c.gridy = 0;
        panel.add(UITheme.formLabel("Enter amount to deposit"), c);
        JTextField amountField = UITheme.textField();
        c.gridy = 1;
        panel.add(amountField, c);
        JButton confirmBtn = UITheme.primaryButton("Deposit");
        c.gridy = 2;
        c.insets = new Insets(16, 0, 0, 0);
        panel.add(confirmBtn, c);
        setContentPane(panel);
        confirmBtn.addActionListener(e -> {
            String text = amountField.getText().trim();
            double amount;
            try {
                amount = Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                UITheme.error(this, "Enter a valid amount.");
                return;
            }
            if (amount <= 0) {
                UITheme.error(this, "Amount must be greater than zero.");
                return;
            }
            account.deposit(amount);
            BankDataStore.getInstance().save();
            onSuccess.run();
            UITheme.info(this, "Deposit successful!\nNew Balance: " + String.format("%.2f", account.getBalance()));
            dispose();
        });
    }
}
// =====================================================================
//  WITHDRAW
// =====================================================================
class WithdrawDialog extends JDialog {
    public WithdrawDialog(JFrame parent, Account account, Runnable onSuccess) {
        super(parent, "Withdraw", true);
        setSize(360, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        c.gridy = 0;
        panel.add(UITheme.formLabel("Enter amount to withdraw"), c);
        JTextField amountField = UITheme.textField();
        c.gridy = 1;
        panel.add(amountField, c);
        c.gridy = 2;
        panel.add(UITheme.formLabel("Confirm PIN"), c);
        JPasswordField pinField = UITheme.passwordField();
        c.gridy = 3;
        panel.add(pinField, c);
        JButton confirmBtn = UITheme.primaryButton("Withdraw");
        c.gridy = 4;
        c.insets = new Insets(16, 0, 0, 0);
        panel.add(confirmBtn, c);
        setContentPane(panel);
        confirmBtn.addActionListener(e -> {
            String text = amountField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();
            double amount;
            try {
                amount = Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                UITheme.error(this, "Enter a valid amount.");
                return;
            }
            if (amount <= 0) {
                UITheme.error(this, "Amount must be greater than zero.");
                return;
            }
            if (!account.verifyPin(pin)) {
                UITheme.error(this, "Incorrect PIN.");
                return;
            }
            if (!account.withdraw(amount)) {
                UITheme.error(this, "Insufficient balance.");
                return;
            }
            BankDataStore.getInstance().save();
            onSuccess.run();
            UITheme.info(this, "Withdrawal successful!\nNew Balance: " + String.format("%.2f", account.getBalance()));
            dispose();
        });
    }
}
// =====================================================================
//  FAST CASH
// =====================================================================
class FastCashDialog extends JDialog {
    private static final int[] PRESETS = {500, 1000, 2000, 5000, 10000, 20000};
    public FastCashDialog(JFrame parent, Account account, Runnable onSuccess) {
        super(parent, "Fast Cash", true);
        setSize(380, 320);
        setLocationRelativeTo(parent);
        setResizable(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel title = UITheme.formLabel("Select an amount for quick withdrawal");
        title.setFont(UITheme.HEADER_FONT.deriveFont(15f));
        panel.add(title, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setBackground(UITheme.BG);
        grid.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        for (int amount : PRESETS) {
            JButton btn = UITheme.primaryButton(String.valueOf(amount));
            btn.addActionListener(e -> withdraw(account, amount, onSuccess));
            grid.add(btn);
        }
        panel.add(grid, BorderLayout.CENTER);
        JButton cancelBtn = UITheme.accentButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        JPanel south = new JPanel();
        south.setBackground(UITheme.BG);
        south.add(cancelBtn);
        panel.add(south, BorderLayout.SOUTH);

        setContentPane(panel);
    }
    private void withdraw(Account account, int amount, Runnable onSuccess) {
        if (!account.withdraw(amount)) {
            UITheme.error(this, "Insufficient balance for " + amount + ".");
            return;
        }
        BankDataStore.getInstance().save();
        onSuccess.run();
        UITheme.info(this, "Dispensed: " + amount
                + "\nNew Balance: " + String.format("%.2f", account.getBalance()));
        dispose();
    }
}
// =====================================================================
//  MINI STATEMENT
// =====================================================================
class MiniStatementDialog extends JDialog {
    public MiniStatementDialog(JFrame parent, Account account) {
        super(parent, "Mini Statement", true);
        setSize(500, 420);
        setLocationRelativeTo(parent);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel title = UITheme.formLabel("Last 10 Transactions - Acc No: " + account.getAccountNumber());
        title.setFont(UITheme.HEADER_FONT.deriveFont(15f));
        panel.add(title, BorderLayout.NORTH);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        List<String> tx = account.getTransactions();
        if (tx.isEmpty()) {
            area.setText("No transactions yet.");
        } else {
            int start = Math.max(0, tx.size() - 10);
            StringBuilder sb = new StringBuilder();
            for (int i = tx.size() - 1; i >= start; i--) {
                sb.append(tx.get(i)).append("\n");
            }
            area.setText(sb.toString());
        }
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        JButton closeBtn = UITheme.primaryButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel south = new JPanel();
        south.setBackground(UITheme.BG);
        south.add(closeBtn);
        panel.add(south, BorderLayout.SOUTH);
        setContentPane(panel);
    }
}
// =====================================================================
//  CHANGE PIN
// =====================================================================
class ChangePinDialog extends JDialog {
    public ChangePinDialog(JFrame parent, Account account) {
        super(parent, "Change PIN", true);
        setSize(360, 320);
        setLocationRelativeTo(parent);
        setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        c.gridy = 0;
        panel.add(UITheme.formLabel("Current PIN"), c);
        JPasswordField oldPinField = UITheme.passwordField();
        c.gridy = 1;
        panel.add(oldPinField, c);
        c.gridy = 2;
        panel.add(UITheme.formLabel("New 4-digit PIN"), c);
        JPasswordField newPinField = UITheme.passwordField();
        c.gridy = 3;
        panel.add(newPinField, c);
        c.gridy = 4;
        panel.add(UITheme.formLabel("Confirm New PIN"), c);
        JPasswordField confirmPinField = UITheme.passwordField();
        c.gridy = 5;
        panel.add(confirmPinField, c);
        JButton confirmBtn = UITheme.primaryButton("Update PIN");
        c.gridy = 6;
        c.insets = new Insets(16, 0, 0, 0);
        panel.add(confirmBtn, c);
      setContentPane(panel);
        confirmBtn.addActionListener(e -> {
            String oldPin = new String(oldPinField.getPassword()).trim();
            String newPin = new String(newPinField.getPassword()).trim();
            String confirmPin = new String(confirmPinField.getPassword()).trim();
            if (!newPin.matches("\\d{4}")) {
                UITheme.error(this, "New PIN must be exactly 4 digits.");
                return;
            }
            if (!newPin.equals(confirmPin)) {
                UITheme.error(this, "New PIN and confirmation do not match.");
                return;
            }
            if (!account.changePin(oldPin, newPin)) {
                UITheme.error(this, "Current PIN is incorrect.");
                return;
            }
            BankDataStore.getInstance().save();
            UITheme.info(this, "PIN updated successfully.");
            dispose();
        });
    }
}
