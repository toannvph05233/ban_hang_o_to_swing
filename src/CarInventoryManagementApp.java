import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CarInventoryManagementApp {
    private JFrame frame;
    private DefaultTableModel carTableModel;
    private DefaultTableModel invoiceTableModel;
    private JTable carTable;
    private JTable invoiceTable;
    private JTextField idField;
    private JTextField nationField;
    private JTextField nameField;
    private JTextField companyField;
    private JTextField priceField;
    private JTextField createDateField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton createInvoiceButton;
    private JButton showAllInvoiceButton;

    private ArrayList<Car> cars;
    private ArrayList<Invoice> invoices;
    private String dataFileName = "car_inventory.txt";
    private String invoiceFileName = "invoices.txt";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CarInventoryManagementApp window = new CarInventoryManagementApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CarInventoryManagementApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Car Inventory Management");
        frame.setBounds(100, 100, 900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cars = new ArrayList<>();
        invoices = new ArrayList<>();
        loadCarsFromFile();
        loadInvoicesFromFile();

        JLabel carInventoryLabel = new JLabel("Car Inventory");
        carInventoryLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        carInventoryLabel.setBounds(10, 10, 150, 20);
        frame.getContentPane().add(carInventoryLabel);

        JLabel invoiceLabel = new JLabel("Invoices");
        invoiceLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        invoiceLabel.setBounds(10, 255, 100, 20);
        frame.getContentPane().add(invoiceLabel);

        String[] carColumnNames = {"ID", "Nation", "Name", "Company", "Price", "Create Date", "Detail"};
        carTableModel = new DefaultTableModel(carColumnNames, 0);
        carTable = new JTable(carTableModel);
        JScrollPane carScrollPane = new JScrollPane(carTable);
        carScrollPane.setBounds(10, 40, 860, 200);
        frame.getContentPane().add(carScrollPane);

        String[] invoiceColumnNames = {"Invoice ID", "Name Customer", "Car Name", "Quantity", "Total Price", "Phone Customer", "Create Date", "Delete"};
        invoiceTableModel = new DefaultTableModel(invoiceColumnNames, 0);
        invoiceTable = new JTable(invoiceTableModel);
        JScrollPane invoiceScrollPane = new JScrollPane(invoiceTable);
        invoiceScrollPane.setBounds(10, 290, 860, 150);
        frame.getContentPane().add(invoiceScrollPane);


        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(10, 480, 60, 20);
        frame.getContentPane().add(idLabel);
        idField = new JTextField();
        idField.setBounds(80, 480, 80, 20);
        frame.getContentPane().add(idField);

        JLabel nationLabel = new JLabel("Nation:");
        nationLabel.setBounds(180, 480, 60, 20);
        frame.getContentPane().add(nationLabel);
        nationField = new JTextField();
        nationField.setBounds(230, 480, 100, 20);
        frame.getContentPane().add(nationField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(350, 480, 60, 20);
        frame.getContentPane().add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(400, 480, 140, 20);
        frame.getContentPane().add(nameField);

        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setBounds(550, 480, 80, 20);
        frame.getContentPane().add(companyLabel);
        companyField = new JTextField();
        companyField.setBounds(630, 480, 100, 20);
        frame.getContentPane().add(companyField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(10, 520, 60, 20);
        frame.getContentPane().add(priceLabel);
        priceField = new JTextField();
        priceField.setBounds(80, 520, 100, 20);
        frame.getContentPane().add(priceField);

        JLabel createDateLabel = new JLabel("Create Date (YYYY-MM-DD):");
        createDateLabel.setBounds(250, 520, 180, 20);
        frame.getContentPane().add(createDateLabel);
        createDateField = new JTextField();
        createDateField.setBounds(430, 520, 150, 20);
        frame.getContentPane().add(createDateField);

        addButton = new JButton("Add");
        addButton.setBounds(10, 580, 80, 30);
        frame.getContentPane().add(addButton);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCar();
            }
        });

        showAllInvoiceButton = new JButton("View All Invoices");
        showAllInvoiceButton.setBounds(590, 580, 150, 30);
        frame.getContentPane().add(showAllInvoiceButton);
        showAllInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewInvoices(invoices);
            }
        });

        updateButton = new JButton("Update");
        updateButton.setBounds(100, 580, 80, 30);
        frame.getContentPane().add(updateButton);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCar();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(190, 580, 80, 30);
        frame.getContentPane().add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCar();
            }
        });

        createInvoiceButton = new JButton("Create Invoice");
        createInvoiceButton.setBounds(450, 580, 120, 30);
        frame.getContentPane().add(createInvoiceButton);
        createInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createInvoice();
            }
        });


        // Khi nhấn vào car show edit
        carTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                showCarEdit();
            }
        });

        frame.getContentPane().setLayout(null);
        populateTable();
        viewInvoices(invoices);
        frame.setVisible(true);
    }

    private void deleteInvoice(int row) {
        if (row >= 0 && row < invoices.size()) {
            invoices.remove(row);
            viewInvoices(invoices);
            saveInvoiceToFile();
        }
    }

    private void viewInvoices(ArrayList<Invoice> invoices) {
        // Tạo nút Xóa và thêm vào cột cuối của bảng hóa đơn
        Action deleteAction = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow != -1) {
                    int dialogResult = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this invoice?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        deleteInvoice(selectedRow);
                    }
                }
            }
        };
        ButtonColumn buttonColumn = new ButtonColumn(invoiceTable, deleteAction, 7);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        invoiceTableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng hóa đơn
        for (Invoice invoice : invoices) {
            Car car = invoice.getCar();
            invoiceTableModel.addRow(new Object[]{invoice.getId(), invoice.getNameCustomer(), car.getName(), invoice.getQuantity(), invoice.getTotalPrice(), invoice.getPhoneCustomer(), new SimpleDateFormat("yyyy-MM-dd").format(invoice.getCreateDate()), "Delete"});
        }
    }


    private void loadInvoicesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    int invoiceId = Integer.parseInt(parts[0]);
                    int carId = Integer.parseInt(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    Date createDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[3]);
                    double totalPrice = Double.parseDouble(parts[4]);
                    String nameCustomer = parts[5];
                    String phoneCustomer = parts[6];
                    // Tìm xe dựa vào carId
                    Car car = findCarById(carId);

                    if (car != null) {
                        invoices.add(new Invoice(invoiceId, car, quantity, createDate, totalPrice, nameCustomer, phoneCustomer));
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Phương thức tìm xe theo ID
    private Car findCarById(int carId) {
        for (Car car : cars) {
            if (car.getId() == carId) {
                return car;
            }
        }
        return null; // Không tìm thấy xe
    }


    // Trong phương thức createInvoice của lớp CarInventoryManagementApp
    private void createInvoice() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Select a car to create an invoice.");
            return;
        }

        // Tạo một JPanel để chứa các thành phần nhập liệu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Thêm các JLabel và JTextField cho số lượng, tên khách hàng và số điện thoại khách hàng
        JTextField quantityField = new JTextField();
        JTextField nameCustomer = new JTextField();
        JTextField phoneCustomer = new JTextField();

        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Name Customer:"));
        panel.add(nameCustomer);
        panel.add(new JLabel("Phone Customer:"));
        panel.add(phoneCustomer);


        // Hiển thị hộp thoại để nhập số lượng và tên khách hàng
        int result = JOptionPane.showConfirmDialog(frame, panel, "Create Invoice", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String quantityStr = quantityField.getText();
            String nameCustomerStr = nameCustomer.getText();
            String phoneCustomerStr = phoneCustomer.getText();

            if (quantityStr.isEmpty() || nameCustomerStr.isEmpty() || phoneCustomerStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Both quantity and customer information are required.");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            Car selectedCar = cars.get(selectedRow);
            double totalPrice = quantity * selectedCar.getPrice();

            String invoiceDetails = "Invoice\n\n" +
                    "Car ID: " + selectedCar.getId() + "\n" +
                    "Car Name: " + selectedCar.getName() + "\n" +
                    "Quantity: " + quantity + "\n" +
                    "Total Price: $" + totalPrice + "\n\n" +
                    "Customer Information:\n" + nameCustomerStr + " - " + phoneCustomerStr;

            // Hiển thị thông tin hóa đơn
            JOptionPane.showMessageDialog(frame, invoiceDetails);

            // Tạo đối tượng Invoice và thêm vào danh sách invoices
            int invoiceId = invoices.get(invoices.size() - 1).getId() + 1; // Tính toán ID hóa đơn dựa trên kích thước danh sách hiện tại
            invoices.add(new Invoice(invoiceId, selectedCar, quantity, new Date(), totalPrice, nameCustomerStr, phoneCustomerStr));
            viewInvoices(invoices); // Cập nhật bảng hóa đơn
            saveInvoiceToFile();
        }
    }

    private void saveInvoiceToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invoiceFileName))) {
            // Ghi thông tin hóa đơn vào tệp tin invoices.txt
            for (Invoice invoice : invoices) {
                bw.write(invoice.getId() + "," + invoice.getCar().getId() + "," +
                        invoice.getQuantity() + "," + new SimpleDateFormat("yyyy-MM-dd").format(invoice.getCreateDate()) + "," + invoice.getTotalPrice() + "," + invoice.getNameCustomer() + "," + invoice.getPhoneCustomer());
                bw.newLine(); // Xuống dòng để ghi hóa đơn tiếp theo
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void viewDetails() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Select a car to view details.");
            return;
        }

        Car selectedCar = cars.get(selectedRow);
        String details = "Car ID: " + selectedCar.getId() + "\n" +
                "Nation: " + selectedCar.getNation() + "\n" +
                "Car Name: " + selectedCar.getName() + "\n" +
                "Company: " + selectedCar.getCompany() + "\n" +
                "Price: $" + selectedCar.getPrice() + "\n" +
                "Create Date: " + new SimpleDateFormat("yyyy-MM-dd").format(selectedCar.getCreateDate());

        JOptionPane.showMessageDialog(frame, details, "Car Details", JOptionPane.PLAIN_MESSAGE);
    }

    private void loadCarsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String nation = parts[1];
                    String name = parts[2];
                    String company = parts[3];
                    double price = Double.parseDouble(parts[4]);
                    Date createDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[5]);
                    cars.add(new Car(id, nation, name, company, price, createDate));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public void showInvoiceByCar(int idCar) {
        ArrayList<Invoice> invoicesByCar = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getCar().getId() == idCar) {
                invoicesByCar.add(invoice);
            }
        }
        viewInvoices(invoicesByCar);

    }

    public void showCarEdit() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Lấy dữ liệu từ hàng đã chọn
            int id = (int) carTableModel.getValueAt(selectedRow, 0);
            String nation = (String) carTableModel.getValueAt(selectedRow, 1);
            String name = (String) carTableModel.getValueAt(selectedRow, 2);
            String company = (String) carTableModel.getValueAt(selectedRow, 3);
            double price = (double) carTableModel.getValueAt(selectedRow, 4);
            String createDateS = (String) carTableModel.getValueAt(selectedRow, 5);
            Date createDate = null;
            try {
                createDate = new SimpleDateFormat("yyyy-MM-dd").parse(createDateS);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            // Đổ dữ liệu vào các JTextField
            idField.setText(String.valueOf(id));
            nationField.setText(nation);
            nameField.setText(name);
            companyField.setText(company);
            priceField.setText(String.valueOf(price));
            createDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(createDate));
            showInvoiceByCar(id);

        }
    }


    private void saveCarsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFileName))) {
            for (Car car : cars) {
                bw.write(car.getId() + "," + car.getNation() + "," + car.getName() + "," + car.getCompany() + "," + car.getPrice() + "," + new SimpleDateFormat("yyyy-MM-dd").format(car.getCreateDate()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateTable() {
        // Tạo nút Xóa và thêm vào cột cuối của bảng hóa đơn
        Action showDetail = new AbstractAction("Detail") {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDetails();
            }
        };
        ButtonColumn buttonColumn = new ButtonColumn(carTable, showDetail, carTable.getColumnCount() - 1);
        buttonColumn.setMnemonic(KeyEvent.VK_D);


        carTableModel.setRowCount(0);
        for (Car car : cars) {
            carTableModel.addRow(new Object[]{car.getId(), car.getNation(), car.getName(), car.getCompany(), car.getPrice(), new SimpleDateFormat("yyyy-MM-dd").format(car.getCreateDate()), "Detail"});
        }
    }

    private void addCar() {
        try {
            int id = Integer.parseInt(idField.getText());
            if (checkId(id)) {
                JOptionPane.showMessageDialog(frame, "Id đã trùng rồi.");
                return;
            }
            String nation = nationField.getText();
            String name = nameField.getText();
            String company = companyField.getText();
            double price = Double.parseDouble(priceField.getText());
            Date createDate = null;
            createDate = new SimpleDateFormat("yyyy-MM-dd").parse(createDateField.getText());
            Car car = new Car(id, nation, name, company, price, createDate);
            cars.add(car);
            carTableModel.addRow(new Object[]{car.getId(), car.getNation(), car.getName(), car.getCompany(), car.getPrice(), new SimpleDateFormat("yyyy-MM-dd").format(car.getCreateDate())});
            clearFields();
            saveCarsToFile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Lỗi nhập dữ liệu (id phải là số).");

        }
    }

    private boolean checkId(int id) {
        for (Car car : cars) {
            if (car.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void updateCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Select a car to update.");
            return;
        }

        int id = Integer.parseInt(idField.getText());
        String nation = nationField.getText();
        String name = nameField.getText();
        String company = companyField.getText();
        double price = Double.parseDouble(priceField.getText());
        Date createDate = null;
        try {
            createDate = new SimpleDateFormat("yyyy-MM-dd").parse(createDateField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Car updatedCar = new Car(id, nation, name, company, price, createDate);
        cars.set(selectedRow, updatedCar);
        carTableModel.setValueAt(updatedCar.getId(), selectedRow, 0);
        carTableModel.setValueAt(updatedCar.getNation(), selectedRow, 1);
        carTableModel.setValueAt(updatedCar.getName(), selectedRow, 2);
        carTableModel.setValueAt(updatedCar.getCompany(), selectedRow, 3);
        carTableModel.setValueAt(updatedCar.getPrice(), selectedRow, 4);
        carTableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(updatedCar.getCreateDate()), selectedRow, 5);

        clearFields();
        saveCarsToFile();
    }

    private void deleteCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Select a car to delete.");
            return;
        }

        cars.remove(selectedRow);
        carTableModel.removeRow(selectedRow);
        clearFields();
        saveCarsToFile();
        JOptionPane.showMessageDialog(frame, "Xóa thành công.");

    }

    private void clearFields() {
        idField.setText("");
        nationField.setText("");
        nameField.setText("");
        companyField.setText("");
        priceField.setText("");
        createDateField.setText("");
    }
}


