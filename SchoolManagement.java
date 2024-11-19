import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SchoolManagement {
    private static final String PASSWORD = "dsceclg@123";

    public static void main(String[] args) {
        // Login Window
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new GridLayout(3, 2));

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JCheckBox showPasswordCheck = new JCheckBox("Show Password");
        JButton loginButton = new JButton("Login");

        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        loginButton.addActionListener(e -> {
            String enteredPassword = new String(passwordField.getPassword());
            if (enteredPassword.equals(PASSWORD)) {
                loginFrame.dispose();
                mainWindow();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(showPasswordCheck);
        loginFrame.add(new JLabel()); // Placeholder
        loginFrame.add(new JLabel()); // Placeholder
        loginFrame.add(loginButton);

        loginFrame.setVisible(true);
    }

    private static void mainWindow() {
        // Main Window
        JFrame mainFrame = new JFrame("School Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 400);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Category", "Name", "ID", "Total Amount", "Paid", "Left"}, 0);
        JTable table = new JTable(model);
        loadExistingData(model);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton addButton = new JButton("Add New Data");
        JButton editButton = new JButton("Edit Selected Data");
        JButton saveToNotepadButton = new JButton("Save to Notepad");

        // Add New Data Button
        addButton.addActionListener(e -> openAddNewDataWindow(model));

        // Edit Selected Data Button
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                openEditDataWindow(model, selectedRow);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a row to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Save to Notepad Button
        saveToNotepadButton.addActionListener(e -> saveToNotepad(model));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(saveToNotepadButton);

        mainFrame.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private static void openAddNewDataWindow(DefaultTableModel model) {
        JFrame addFrame = new JFrame("Add New Data");
        addFrame.setSize(400, 400);
        addFrame.setLayout(new GridLayout(7, 2));

        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Student", "Staff", "Faculty"});
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        JLabel amountLabel = new JLabel("Total Amount:");
        JTextField amountField = new JTextField();
        JLabel paidLabel = new JLabel("Paid Amount:");
        JTextField paidField = new JTextField();
        JLabel leftLabel = new JLabel("Left Amount:");
        JTextField leftField = new JTextField();
        leftField.setEditable(false);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                String category = (String) categoryComboBox.getSelectedItem();
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                int totalAmount = Integer.parseInt(amountField.getText().trim());
                int paidAmount = Integer.parseInt(paidField.getText().trim());
                int leftAmount = totalAmount - paidAmount;

                leftField.setText(String.valueOf(leftAmount));
                model.addRow(new Object[]{category, name, id, totalAmount, paidAmount, leftAmount});
                saveUpdatedData(model);
                addFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addFrame, "Please enter valid numeric values for amounts.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addFrame.add(categoryLabel);
        addFrame.add(categoryComboBox);
        addFrame.add(nameLabel);
        addFrame.add(nameField);
        addFrame.add(idLabel);
        addFrame.add(idField);
        addFrame.add(amountLabel);
        addFrame.add(amountField);
        addFrame.add(paidLabel);
        addFrame.add(paidField);
        addFrame.add(leftLabel);
        addFrame.add(leftField);
        addFrame.add(new JLabel()); // Placeholder
        addFrame.add(addButton);

        addFrame.setVisible(true);
    }

    private static void openEditDataWindow(DefaultTableModel model, int selectedRow) {
        JFrame editFrame = new JFrame("Edit Data");
        editFrame.setSize(400, 400);
        editFrame.setLayout(new GridLayout(7, 2));

        String category = (String) model.getValueAt(selectedRow, 0);
        String name = (String) model.getValueAt(selectedRow, 1);
        String id = (String) model.getValueAt(selectedRow, 2);
        int totalAmount = (int) model.getValueAt(selectedRow, 3);
        int paidAmount = (int) model.getValueAt(selectedRow, 4);
        int leftAmount = totalAmount - paidAmount;

        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Student", "Staff", "Faculty"});
        categoryComboBox.setSelectedItem(category);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(name);
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField(id);
        JLabel amountLabel = new JLabel("Total Amount:");
        JTextField amountField = new JTextField(String.valueOf(totalAmount));
        JLabel paidLabel = new JLabel("Paid Amount:");
        JTextField paidField = new JTextField(String.valueOf(paidAmount));
        JLabel leftLabel = new JLabel("Left Amount:");
        JTextField leftField = new JTextField(String.valueOf(leftAmount));
        leftField.setEditable(false);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String newCategory = (String) categoryComboBox.getSelectedItem();
                String newName = nameField.getText().trim();
                String newId = idField.getText().trim();
                int newTotalAmount = Integer.parseInt(amountField.getText().trim());
                int newPaidAmount = Integer.parseInt(paidField.getText().trim());
                int newLeftAmount = newTotalAmount - newPaidAmount;

                model.setValueAt(newCategory, selectedRow, 0);
                model.setValueAt(newName, selectedRow, 1);
                model.setValueAt(newId, selectedRow, 2);
                model.setValueAt(newTotalAmount, selectedRow, 3);
                model.setValueAt(newPaidAmount, selectedRow, 4);
                model.setValueAt(newLeftAmount, selectedRow, 5);

                saveUpdatedData(model);
                editFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editFrame, "Please enter valid numeric values for amounts.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editFrame.add(categoryLabel);
        editFrame.add(categoryComboBox);
        editFrame.add(nameLabel);
        editFrame.add(nameField);
        editFrame.add(idLabel);
        editFrame.add(idField);
        editFrame.add(amountLabel);
        editFrame.add(amountField);
        editFrame.add(paidLabel);
        editFrame.add(paidField);
        editFrame.add(leftLabel);
        editFrame.add(leftField);
        editFrame.add(new JLabel()); // Placeholder
        editFrame.add(saveButton);

        editFrame.setVisible(true);
    }

    private static void saveUpdatedData(DefaultTableModel model) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("school_data.txt"))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    row.append(model.getValueAt(i, j)).append(",");
                }
                bw.write(row.substring(0, row.length() - 1));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadExistingData(DefaultTableModel model) {
        File file = new File("school_data.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    model.addRow(new Object[]{data[0], data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5])});
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveToNotepad(DefaultTableModel model) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("school_data_export.txt"))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    row.append(model.getValueAt(i, j)).append("\t");
                }
                bw.write(row.toString().trim());
                bw.newLine();
            }
            JOptionPane.showMessageDialog(null, "Data saved successfully to school_data_export.txt.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}