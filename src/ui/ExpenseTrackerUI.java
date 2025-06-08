package ui;
import model.Expense;
import dao.ExpenseDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseTrackerUI extends JFrame {
    private JTextField amountField, categoryField;
    private JTextArea descriptionArea;
    private JButton addButton, viewButton;
    private ExpenseDAO expenseDAO = new ExpenseDAO();

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(64, 123, 255);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color DARK_GRAY = new Color(52, 58, 64);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.black;
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public ExpenseTrackerUI() {
        initializeUI();
        setupLookAndFeel();
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Custom UI properties for modern look
            UIManager.put("Button.font", BODY_FONT);
            UIManager.put("Label.font", BODY_FONT);
            UIManager.put("TextField.font", BODY_FONT);
            UIManager.put("TextArea.font", BODY_FONT);
            UIManager.put("Table.font", BODY_FONT);
            UIManager.put("TableHeader.font", SUBTITLE_FONT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("💰 Expense Tracker Pro");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern background
        getContentPane().setBackground(LIGHT_GRAY);

        // Create main container with modern layout
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setBackground(LIGHT_GRAY);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = createFormPanel();
        mainContainer.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(mainContainer);

        // Add window border
        getRootPane().setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        // Title
        JLabel titleLabel = new JLabel("💰 Expense Tracker Pro");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(DARK_GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Track your expenses with style and precision");
        subtitleLabel.setFont(BODY_FONT);
        subtitleLabel.setForeground(SECONDARY_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout(0, 5));
        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Add subtle border
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(30, 30, 20, 30)
        ));

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.WEST;

        // Amount field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(createStyledLabel("💵 Amount"), gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        amountField = createStyledTextField("0.00");
        formPanel.add(amountField, gbc);

        // Category field
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(createStyledLabel("📁 Category"), gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        categoryField = createStyledTextField("Food, Transport, Entertainment...");
        formPanel.add(categoryField, gbc);

        // Description field
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(createStyledLabel("📝 Description"), gbc);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        descriptionArea = createStyledTextArea("Enter expense details...");
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.setPreferredSize(new Dimension(0, 100));
        formPanel.add(scrollPane, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        // Add button
        addButton = createStyledButton("➕ Add Expense", PRIMARY_COLOR, BLACK);
        addButton.addActionListener(e -> addExpense());
        buttonPanel.add(addButton);

        // View button
        viewButton = createStyledButton("👁 View Expenses", SECONDARY_COLOR, BLACK);
        viewButton.addActionListener(e -> viewExpenses());
        buttonPanel.add(viewButton);

        return buttonPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(DARK_GRAY);
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(WHITE);
        field.setForeground(DARK_GRAY);

        // Add placeholder effect
        field.setText(placeholder);
        field.setForeground(SECONDARY_COLOR);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(DARK_GRAY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(SECONDARY_COLOR);
                }
            }
        });

        return field;
    }

    private JTextArea createStyledTextArea(String placeholder) {
        JTextArea area = new JTextArea(4, 20);
        area.setFont(BODY_FONT);
        area.setBorder(new EmptyBorder(12, 15, 12, 15));
        area.setBackground(WHITE);
        area.setForeground(DARK_GRAY);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        // Add placeholder effect
        area.setText(placeholder);
        area.setForeground(SECONDARY_COLOR);
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(DARK_GRAY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(placeholder);
                    area.setForeground(SECONDARY_COLOR);
                }
            }
        });

        return area;
    }

    private JButton createVisibleButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);

        // Add hover effect with better visibility
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor, 2),
                        BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker(), 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        return button;
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void addExpense() {
        try {
            String amountText = amountField.getText();
            String categoryText = categoryField.getText();
            String descriptionText = descriptionArea.getText();

            // Check for placeholder text
            if (amountText.equals("0.00") || amountText.trim().isEmpty()) {
                showStyledMessage("⚠ Please enter a valid amount!", WARNING_COLOR);
                return;
            }

            if (categoryText.equals("Food, Transport, Entertainment...") || categoryText.trim().isEmpty()) {
                showStyledMessage("⚠ Please enter a category!", WARNING_COLOR);
                return;
            }

            if (descriptionText.equals("Enter expense details...")) {
                descriptionText = "";
            }

            double amount = Double.parseDouble(amountText);

            Expense expense = new Expense(amount, categoryText, descriptionText, new Date());
            expenseDAO.addExpense(expense);
            showStyledMessage("✅ Expense added successfully!", SUCCESS_COLOR);

            clearFields();

        } catch (NumberFormatException ex) {
            showStyledMessage("❌ Please enter a valid amount!", DANGER_COLOR);
        } catch (Exception ex) {
            showStyledMessage("❌ Error: " + ex.getMessage(), DANGER_COLOR);
        }
    }

    private void viewExpenses() {
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            showExpensesDialog(expenses, "📊 All Expenses");
        } catch (Exception ex) {
            showStyledMessage("❌ Error retrieving expenses: " + ex.getMessage(), DANGER_COLOR);
        }
    }

    private void showExpensesDialog(List<Expense> expenses, String title) {
        if (expenses.isEmpty()) {
            showStyledMessage("📭 No expenses found!", WARNING_COLOR);
            return;
        }

        // Create modern dialog
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(LIGHT_GRAY);

        // Create styled table
        String[] columnNames = {"ID", "Amount (₹)", "Category", "Description", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add data to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Expense expense : expenses) {
            Object[] row = {
                    expense.getId(),
                    String.format("%.2f", expense.getAmount()),
                    expense.getCategory(),
                    expense.getDescription(),
                    dateFormat.format(expense.getDate())
            };
            model.addRow(row);
        }

        // Create and style table
        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(WHITE);

        // Header panel with total
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        JLabel totalLabel = new JLabel("💰 Total Expenses: ₹" + String.format("%.2f", total) +
                " | 📊 Count: " + expenses.size());
        totalLabel.setFont(SUBTITLE_FONT);
        totalLabel.setForeground(DARK_GRAY);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(totalLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createExpenseDialogButtons(dialog, table, model, expenses);

        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setSelectionForeground(DARK_GRAY);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setBackground(WHITE);

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(SUBTITLE_FONT);
        header.setBackground(LIGHT_GRAY);
        header.setForeground(DARK_GRAY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        header.setReorderingAllowed(false);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    private JPanel createExpenseDialogButtons(JDialog dialog, JTable table, DefaultTableModel model, List<Expense> expenses) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton editButton = createVisibleButton("✏ Edit", PRIMARY_COLOR, Color.BLACK);
        JButton deleteButton = createVisibleButton("🗑 Delete", DANGER_COLOR, BLACK);
        JButton filterButton = createVisibleButton("🔍 Filter", WARNING_COLOR, BLACK);
        JButton refreshButton = createVisibleButton("🔄 Refresh", SUCCESS_COLOR, BLACK);
        JButton closeButton = createVisibleButton("❌ Close", SECONDARY_COLOR, BLACK);

        // Edit button action
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int expenseId = (Integer) model.getValueAt(selectedRow, 0);
                editExpense(expenseId, expenses);
                dialog.dispose();
                viewExpenses();
            } else {
                showStyledMessage("⚠ Please select an expense to edit!", WARNING_COLOR);
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int expenseId = (Integer) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "🗑 Are you sure you want to delete this expense?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteExpense(expenseId);
                    dialog.dispose();
                    viewExpenses();
                }
            } else {
                showStyledMessage("⚠ Please select an expense to delete!", WARNING_COLOR);
            }
        });

        // Filter button action
        filterButton.addActionListener(e -> {
            dialog.dispose();
            showFilterDialog();
        });

        // Refresh button action
        refreshButton.addActionListener(e -> {
            dialog.dispose();
            viewExpenses();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        return buttonPanel;
    }

    private void editExpense(int expenseId, List<Expense> expenses) {
        // Find the expense to edit
        Expense expenseToEdit = null;
        for (Expense expense : expenses) {
            if (expense.getId() == expenseId) {
                expenseToEdit = expense;
                break;
            }
        }

        if (expenseToEdit == null) return;

        // Create modern edit dialog
        JDialog editDialog = new JDialog(this, "✏ Edit Expense", true);
        editDialog.setSize(500, 400);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout());
        editDialog.getContentPane().setBackground(LIGHT_GRAY);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField editAmountField = new JTextField(String.valueOf(expenseToEdit.getAmount()));
        JTextField editCategoryField = new JTextField(expenseToEdit.getCategory());
        JTextArea editDescriptionArea = new JTextArea(expenseToEdit.getDescription());
        editDescriptionArea.setRows(3);
        editDescriptionArea.setLineWrap(true);
        editDescriptionArea.setWrapStyleWord(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JTextField editDateField = new JTextField(dateFormat.format(expenseToEdit.getDate()));

        // Style fields
        styleEditField(editAmountField);
        styleEditField(editCategoryField);
        styleEditField(editDateField);
        editDescriptionArea.setFont(BODY_FONT);
        editDescriptionArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("💵 Amount"), gbc);
        gbc.gridy++;
        formPanel.add(editAmountField, gbc);

        gbc.gridy++;
        formPanel.add(createStyledLabel("📁 Category"), gbc);
        gbc.gridy++;
        formPanel.add(editCategoryField, gbc);

        gbc.gridy++;
        formPanel.add(createStyledLabel("📝 Description"), gbc);
        gbc.gridy++;
        JScrollPane descScrollPane = new JScrollPane(editDescriptionArea);
        descScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        descScrollPane.setPreferredSize(new Dimension(0, 80));
        formPanel.add(descScrollPane, gbc);

        gbc.gridy++;
        formPanel.add(createStyledLabel("📅 Date (yyyy-mm-dd)"), gbc);
        gbc.gridy++;
        formPanel.add(editDateField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        JButton saveButton = createVisibleButton("💾 Save Changes", SUCCESS_COLOR, BLACK);
        JButton cancelButton = createVisibleButton("❌ Cancel", SECONDARY_COLOR, BLACK);

        saveButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(editAmountField.getText());
                String category = editCategoryField.getText();
                String description = editDescriptionArea.getText();
                Date date = dateFormat.parse(editDateField.getText());

                if (category.trim().isEmpty()) {
                    showStyledMessage("⚠ Please enter a category!", WARNING_COLOR);
                    return;
                }

                Expense updatedExpense = new Expense(amount, category, description, date);
                updatedExpense.setId(expenseId);
                expenseDAO.updateExpense(updatedExpense);

                showStyledMessage("✅ Expense updated successfully!", SUCCESS_COLOR);
                editDialog.dispose();

            } catch (NumberFormatException ex) {
                showStyledMessage("❌ Please enter a valid amount!", DANGER_COLOR);
            } catch (ParseException ex) {
                showStyledMessage("❌ Please enter date in yyyy-mm-dd format!", DANGER_COLOR);
            } catch (Exception ex) {
                showStyledMessage("❌ Error: " + ex.getMessage(), DANGER_COLOR);
            }
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }

    private void styleEditField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        field.setBackground(WHITE);
        field.setForeground(DARK_GRAY);
    }

    private void deleteExpense(int expenseId) {
        try {
            expenseDAO.deleteExpense(expenseId);
            showStyledMessage("✅ Expense deleted successfully!", SUCCESS_COLOR);
        } catch (Exception ex) {
            showStyledMessage("❌ Error deleting expense: " + ex.getMessage(), DANGER_COLOR);
        }
    }

    private void showFilterDialog() {
        JDialog filterDialog = new JDialog(this, "🔍 Filter Expenses", true);
        filterDialog.setSize(500, 350);
        filterDialog.setLocationRelativeTo(this);
        filterDialog.setLayout(new BorderLayout());
        filterDialog.getContentPane().setBackground(LIGHT_GRAY);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;

        JRadioButton categoryFilter = new JRadioButton("📁 Filter by Category");
        JRadioButton dateRangeFilter = new JRadioButton("📅 Filter by Date Range");

        // Style radio buttons
        categoryFilter.setFont(BODY_FONT);
        categoryFilter.setBackground(WHITE);
        categoryFilter.setForeground(DARK_GRAY);
        dateRangeFilter.setFont(BODY_FONT);
        dateRangeFilter.setBackground(WHITE);
        dateRangeFilter.setForeground(DARK_GRAY);

        ButtonGroup filterGroup = new ButtonGroup();
        filterGroup.add(categoryFilter);
        filterGroup.add(dateRangeFilter);

        JTextField startDateField = new JTextField("yyyy-mm-dd");
        JTextField endDateField = new JTextField("yyyy-mm-dd");
        styleEditField(startDateField);
        styleEditField(endDateField);

        // Load categories for dropdown
        try {
            List<String> categories = expenseDAO.getAllCategories();
            JComboBox<String> categoryCombo = new JComboBox<>(categories.toArray(new String[0]));
            categoryCombo.setEditable(true);
            categoryCombo.setFont(BODY_FONT);
            categoryCombo.setBackground(WHITE);

            gbc.gridx = 0; gbc.gridy = 0;
            gbc.gridwidth = 2;
            formPanel.add(categoryFilter, gbc);

            gbc.gridy++;
            gbc.insets = new Insets(0, 20, 20, 0);
            formPanel.add(categoryCombo, gbc);

            gbc.gridy++;
            gbc.insets = new Insets(0, 0, 15, 0);
            formPanel.add(dateRangeFilter, gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 20, 10, 0);
            formPanel.add(createStyledLabel("Start Date:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            formPanel.add(startDateField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            formPanel.add(createStyledLabel("End Date:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            formPanel.add(endDateField, gbc);

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            buttonPanel.setBackground(WHITE);
            buttonPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

            JButton applyButton = createVisibleButton("🔍 Apply Filter", PRIMARY_COLOR, BLACK);
            JButton cancelButton = createVisibleButton("❌ Cancel", SECONDARY_COLOR, BLACK);

            applyButton.addActionListener(e -> {
                try {
                    List<Expense> filteredExpenses = null;
                    String title = "";

                    if (categoryFilter.isSelected()) {
                        String selectedCategory = (String) categoryCombo.getSelectedItem();
                        if (selectedCategory != null && !selectedCategory.trim().isEmpty()) {
                            filteredExpenses = expenseDAO.getExpensesByCategory(selectedCategory);
                            title = "📁 Expenses - Category: " + selectedCategory;
                        }
                    } else if (dateRangeFilter.isSelected()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = dateFormat.parse(startDateField.getText());
                        Date endDate = dateFormat.parse(endDateField.getText());
                        filteredExpenses = expenseDAO.getExpensesByDateRange(startDate, endDate);
                        title = "📅 Expenses - From: " + startDateField.getText() + " To: " + endDateField.getText();
                    }

                    if (filteredExpenses != null) {
                        filterDialog.dispose();
                        showExpensesDialog(filteredExpenses, title);
                    } else {
                        showStyledMessage("⚠ Please select a filter option!", WARNING_COLOR);
                    }

                } catch (ParseException ex) {
                    showStyledMessage("❌ Please enter dates in yyyy-mm-dd format!", DANGER_COLOR);
                } catch (Exception ex) {
                    showStyledMessage("❌ Error: " + ex.getMessage(), DANGER_COLOR);
                }
            });

            cancelButton.addActionListener(e -> filterDialog.dispose());

            buttonPanel.add(applyButton);
            buttonPanel.add(cancelButton);

            filterDialog.add(formPanel, BorderLayout.CENTER);
            filterDialog.add(buttonPanel, BorderLayout.SOUTH);

        } catch (Exception ex) {
            showStyledMessage("❌ Error loading categories: " + ex.getMessage(), DANGER_COLOR);
        }

        filterDialog.setVisible(true);
    }

    private void showStyledMessage(String message, Color color) {
        JDialog messageDialog = new JDialog(this, "Notification", true);
        messageDialog.setSize(400, 150);
        messageDialog.setLocationRelativeTo(this);
        messageDialog.setLayout(new BorderLayout());
        messageDialog.getContentPane().setBackground(WHITE);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(BODY_FONT);
        messageLabel.setForeground(color);
        messageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton okButton = createStyledButton("OK", color, Color.black);
        okButton.addActionListener(e -> messageDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(okButton);

        messageDialog.add(messageLabel, BorderLayout.CENTER);
        messageDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Auto close after 3 seconds
        Timer timer = new Timer(3000, e -> messageDialog.dispose());
        timer.setRepeats(false);
        timer.start();

        messageDialog.setVisible(true);
    }

    private void clearFields() {
        amountField.setText("0.00");
        amountField.setForeground(SECONDARY_COLOR);
        categoryField.setText("Food, Transport, Entertainment...");
        categoryField.setForeground(SECONDARY_COLOR);
        descriptionArea.setText("Enter expense details...");
        descriptionArea.setForeground(SECONDARY_COLOR);
    }
}