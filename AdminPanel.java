package LogisticsManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.sql.SQLException;

public class AdminPanel extends JPanel {
    private User currentUser;
    private Analytics analytics;
    private WarehouseManager warehouseManager;

    public AdminPanel(User user) {
        this.currentUser = user;
        try {
            this.analytics = new Analytics();
            this.warehouseManager = new WarehouseManager();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }

        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Dashboard Panel
        tabbedPane.addTab("Dashboard", createDashboardPanel());

        // Warehouse Management Panel
        tabbedPane.addTab("Warehouse Management", createWarehousePanel());

        // Transportation Management Panel
        tabbedPane.addTab("Transportation", createTransportationPanel());

        // Analytics Panel
        tabbedPane.addTab("Analytics", createAnalyticsPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add Quick Stats Panels
        panel.add(createQuickStatsPanel("Orders Today", "25"));
        panel.add(createQuickStatsPanel("Active Deliveries", "12"));
        panel.add(createQuickStatsPanel("Available Drivers", "8"));
        panel.add(createQuickStatsPanel("Warehouse Capacity", "65%"));

        return panel;
    }

    private JPanel createQuickStatsPanel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createWarehousePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JComboBox<String> warehouseSelector = new JComboBox<>();
        warehouseSelector.addItem("Warehouse 1");
        warehouseSelector.addItem("Warehouse 2");

        panel.add(warehouseSelector, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel tempLabel = new JLabel("Current Temperature: ");
        JLabel tempValue = new JLabel("23°C");

        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(tempLabel, gbc);
        gbc.gridx = 1;
        detailsPanel.add(tempValue, gbc);

        JLabel capacityLabel = new JLabel("Capacity Utilization: ");
        JProgressBar capacityBar = new JProgressBar(0, 100);
        capacityBar.setValue(65);

        gbc.gridx = 0; gbc.gridy = 1;
        detailsPanel.add(capacityLabel, gbc);
        gbc.gridx = 1;
        detailsPanel.add(capacityBar, gbc);

        panel.add(detailsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransportationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Driver ID", "Name", "Vehicle", "Status", "Current Location"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable driverTable = new JTable(model);

        panel.add(new JScrollPane(driverTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton assignButton = new JButton("Assign Driver");
        JButton trackButton = new JButton("Track Delivery");

        controlPanel.add(assignButton);
        controlPanel.add(trackButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel chartsPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        chartsPanel.add(createChartPanel("Monthly Revenue", "Month", "Revenue",
                new String[]{"Jan", "Feb", "Mar"}, new double[]{1000, 1500, 1200}));

        chartsPanel.add(createChartPanel("Delivery Performance", "Month", "On-Time %",
                new String[]{"Jan", "Feb", "Mar"}, new double[]{95, 92, 97}));

        panel.add(chartsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton exportPdfButton = new JButton("Export to PDF");
        JButton exportExcelButton = new JButton("Export to Excel");

        buttonPanel.add(exportPdfButton);
        buttonPanel.add(exportExcelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ChartPanel createChartPanel(String title, String categoryAxisLabel, String valueAxisLabel,
                                        String[] categories, double[] values) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < categories.length; i++) {
            dataset.addValue(values[i], title, categories[i]);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title, categoryAxisLabel, valueAxisLabel, dataset);

        return new ChartPanel(chart);
    }

    // Main method for local testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Panel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            User testUser = new User(1, "admin", "admin", "admin@example.com", "1234567890");
            AdminPanel adminPanel = new AdminPanel(testUser);

            frame.add(adminPanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
