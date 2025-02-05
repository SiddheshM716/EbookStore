import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EbookStoreApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/aoop";
    private static final String USER = "root";
    private static final String PASSWORD = "aptkmnS$2006";

    private static JFrame frame;
    private static CardLayout cardLayout;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EbookStoreApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("EBook Store Platform");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        // Create and add all panels to the frame
        JPanel loginPanel = createLoginPanel();
        JPanel signupPanel = createSignupPanel();
        JPanel homePanel = createHomePanel();

        // Add panels to the frame with names for CardLayout
        frame.add(loginPanel, "Login");
        frame.add(signupPanel, "Signup");
        frame.add(homePanel, "Home");

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
        cardLayout.show(frame.getContentPane(), "Login");
    }

    // Method to create the login panel
    private static JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        JLabel loginTitle = new JLabel("Login", JLabel.CENTER);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set width and height
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set width and height

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        loginButton.addActionListener(e -> handleLogin(usernameField.getText(), new String(passwordField.getPassword())));
        signupButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Signup"));

        // Adding components to the login panel
        addComponent(loginPanel, loginTitle, 0, 0);
        addComponent(loginPanel, new JLabel("Username:"), 0, 1);
        addComponent(loginPanel, usernameField, 1, 1);
        addComponent(loginPanel, new JLabel("Password:"), 0, 2);
        addComponent(loginPanel, passwordField, 1, 2);
        addComponent(loginPanel, loginButton, 0, 3);
        addComponent(loginPanel, signupButton, 1, 3);

        return loginPanel;
    }

    // Method to create the signup panel
    private static JPanel createSignupPanel() {
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridBagLayout());
        JLabel signupTitle = new JLabel("Signup", JLabel.CENTER);

        JTextField signupFullNameField = new JTextField();
        signupFullNameField.setPreferredSize(new Dimension(200, 30)); // Full Name Field

        JTextField signupEmailField = new JTextField();
        signupEmailField.setPreferredSize(new Dimension(200, 30)); // Email Field

        JTextField signupUsernameField = new JTextField();
        signupUsernameField.setPreferredSize(new Dimension(200, 30)); // Username Field

        JPasswordField signupPasswordField1 = new JPasswordField();
        signupPasswordField1.setPreferredSize(new Dimension(200, 30)); // Password Field

        JPasswordField signupPasswordField2 = new JPasswordField();
        signupPasswordField2.setPreferredSize(new Dimension(200, 30)); // Confirm Password Field

        JButton signupSubmitButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        signupSubmitButton.addActionListener(e -> handleSignup(
                signupFullNameField.getText(),
                signupEmailField.getText(),
                signupUsernameField.getText(),
                new String(signupPasswordField1.getPassword()),
                new String(signupPasswordField2.getPassword())
        ));

        backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Login"));

        // Adding components to the signup panel
        addComponent(signupPanel, signupTitle, 0, 0);
        addComponent(signupPanel, new JLabel("Full Name:"), 0, 1);
        addComponent(signupPanel, signupFullNameField, 1, 1);
        addComponent(signupPanel, new JLabel("Email:"), 0, 2);
        addComponent(signupPanel, signupEmailField, 1, 2);
        addComponent(signupPanel, new JLabel("Username:"), 0, 3);
        addComponent(signupPanel, signupUsernameField, 1, 3);
        addComponent(signupPanel, new JLabel("Password:"), 0, 4);
        addComponent(signupPanel, signupPasswordField1, 1, 4);
        addComponent(signupPanel, new JLabel("Confirm Password:"), 0, 5);
        addComponent(signupPanel, signupPasswordField2, 1, 5);
        addComponent(signupPanel, signupSubmitButton, 0, 6);
        addComponent(signupPanel, backButton, 1, 6);

        return signupPanel;
    }

    // Method to create the home panel
    // Assuming you have a CardLayout named 'cardLayout' and JFrame 'frame'
    // Class-level variable to store the logged-in user ID
    private static int loggedInUserId;

    private static JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel homeTitle = new JLabel("Welcome to the EBook Store!", JLabel.CENTER);
        homeTitle.setFont(new Font("Arial", Font.BOLD, 30));

        titlePanel.add(homeTitle);
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

        // Create the search and filter panel
        JPanel searchFilterPanel = new JPanel();
        searchFilterPanel.setLayout(new FlowLayout());

        JButton profileButton = new JButton("Profile");
        profileButton.addActionListener(e -> {
            JPanel profilePanel = createProfilePanel();
            frame.getContentPane().add(profilePanel, "Profile"); // Add profile panel to CardLayout
            cardLayout.show(frame.getContentPane(), "Profile"); // Switch to profile panel
        });

        JTextField searchField = new JTextField(20); // Search bar
        JButton searchButton = new JButton("Search");

        String[] genres = {"All", "Fiction", "Non-Fiction", "Mystery", "Science Fiction", "Fantasy", "Biography"};
        JComboBox<String> genreComboBox = new JComboBox<>(genres); // Dropdown for genres
        JButton filterButton = new JButton("Filter by Genre");

        JButton viewCollectionButton = new JButton("View Collection");
        viewCollectionButton.addActionListener(e -> {
            JPanel viewCollectionPanel = createViewCollectionPanel(loggedInUserId); // Method to create view cart panel
            frame.getContentPane().add(viewCollectionPanel, "ViewCollection"); // Add view cart panel to CardLayout
            cardLayout.show(frame.getContentPane(), "ViewCollection"); // Switch to view cart panel
        });

        // Add View Cart button
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> {
            JPanel viewCartPanel = createViewCartPanel(loggedInUserId); // Method to create view cart panel
            frame.getContentPane().add(viewCartPanel, "ViewCart"); // Add view cart panel to CardLayout
            cardLayout.show(frame.getContentPane(), "ViewCart"); // Switch to view cart panel
        });

        // Action listener for search
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            // Create the search home panel with the search text
            JPanel searchHomePanel = createSearchPanel(searchText);

            // Remove the current panel (home panel) from the CardLayout
            frame.getContentPane().remove(homePanel); // Assuming `homePanel` is the current panel

            // Add the search home panel to the CardLayout
            frame.getContentPane().add(searchHomePanel, "SearchHome");

            // Show the search home panel
            cardLayout.show(frame.getContentPane(), "SearchHome");
        });

        // Action listener for filter
        filterButton.addActionListener(e -> {
            String selectedGenre1 = (String) genreComboBox.getSelectedItem();

            if (selectedGenre1.equals("All")) {
                // If "All" is selected, go back to the home page
                cardLayout.show(frame.getContentPane(), "Home");
            } else {
                try {
                    // Create the filtered genre panel with the selected genre
                    JPanel genrePanel = creategenrepagePanel(selectedGenre1);

                    // Remove the current panel (home panel) from the CardLayout
                    frame.getContentPane().remove(homePanel); // Assuming `homePanel` is the current panel

                    // Add the genre panel to the CardLayout
                    frame.getContentPane().add(genrePanel, "GenreFilter");

                    // Show the genre filter panel
                    cardLayout.show(frame.getContentPane(), "GenreFilter");
                } catch (Exception ex) {
                    // Handle the exception and show an error message
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred while filtering by genre.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add profile button to the search filter panel
        searchFilterPanel.add(profileButton); // Add profile button first
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(searchButton);
        searchFilterPanel.add(genreComboBox);
        searchFilterPanel.add(filterButton);
        searchFilterPanel.add(viewCartButton);
        searchFilterPanel.add(viewCollectionButton);

        // Add search filter panel to the home panel
        homePanel.add(titlePanel);
        homePanel.add(searchFilterPanel); // Add searchFilterPanel first

        // Add genre panels (multiple genres like Fiction, Non-Fiction, etc.)
        try {
            homePanel.add(createGenrePanel("Fiction"));
            homePanel.add(createGenrePanel("Non-Fiction"));
            homePanel.add(createGenrePanel("Mystery"));
            homePanel.add(createGenrePanel("Science Fiction"));
            homePanel.add(createGenrePanel("Fantasy"));
            homePanel.add(createGenrePanel("Biography"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the button panel (logout button)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Login"));

        buttonPanel.add(logoutButton); // Adding the panel with the logout button

        homePanel.add(buttonPanel); // Adding the button panel

        // Wrap homePanel in a JScrollPane for scroll functionality
        JScrollPane scrollPane = new JScrollPane(homePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Vertical scrollbar when needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No horizontal scrollbar
        scrollPane.addMouseWheelListener(e -> {
            // Change the amount scrolled for each mouse wheel event
            int notches = e.getWheelRotation();
            int scrollAmount = 10; // Change this value to increase or decrease scroll speed
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getValue() + notches * scrollAmount);
        });

        // Return the scrollable panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }
    private static JPanel createViewCartPanel(int loggedInUserId) {
        JPanel viewCartPanel = new JPanel();
        viewCartPanel.setLayout(new BorderLayout());

        JLabel cartTitle = new JLabel("Your Cart", JLabel.CENTER);
        cartTitle.setFont(new Font("Arial", Font.BOLD, 24));
        viewCartPanel.add(cartTitle, BorderLayout.NORTH);

        // Create a panel for cart items with a vertical BoxLayout
        JPanel cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));

        double totalAmount = 0.0;
        boolean cartIsEmpty = true;
        java.util.List<JCheckBox> checkBoxList = new ArrayList<>(); // List to store checkboxes

        // Query to fetch the cart details along with book information
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT b.id, b.title, b.author, b.price, b.genre, b.publication_date, b.description, c.added_at " +
                             "FROM cart c JOIN books b ON c.book_id = b.id " +
                             "WHERE c.user_id = ?")) {

            stmt.setInt(1, loggedInUserId); // Fetch cart items for the logged-in user
            ResultSet rs = stmt.executeQuery();

            // Check if the cart has items
            if (!rs.isBeforeFirst()) { // No data in cart
                JLabel emptyMessage = new JLabel("Your cart is currently empty.", JLabel.CENTER);
                viewCartPanel.add(emptyMessage, BorderLayout.CENTER);
                JButton backButton = new JButton("Back to Home");
                backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home"));
                viewCartPanel.add(backButton,BorderLayout.SOUTH);
            } else {
                cartIsEmpty = false; // Cart has items
                // Create an outer panel to hold all book panels
                // Create an outer panel to hold all book panels
                JPanel booksInCart = new JPanel();
                booksInCart.setLayout(new BoxLayout(booksInCart, BoxLayout.Y_AXIS)); // Vertical stacking


                while (rs.next()) {
                    // Fetch book details and create a Book object
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPrice(rs.getBigDecimal("price"));
                    book.setGenre(rs.getString("genre"));
                    book.setPublicationDate(rs.getDate("publication_date"));
                    book.setDescription(rs.getString("description"));

                    String addedAt = rs.getString("added_at");

                    // Create a panel for each book with FlowLayout
                    JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout to align to the left

                    // Cover Image
                    String coverImagePath = "/Users/siddheshm/Documents/Advanced OOPS/books/" + book.getId() + ".jpg";
                    ImageIcon bookCoverIcon = new ImageIcon(coverImagePath);
                    JLabel coverImageLabel = new JLabel();
                    coverImageLabel.setIcon(new ImageIcon(bookCoverIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
                    coverImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // No border

                    // Make the cover image clickable
                    coverImageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            // Navigate to showBookDetails with the book object
                            showBookDetails(book);
                        }
                    });

                    // Create a panel for book details
                    JPanel bookDetailsPanel = new JPanel();
                    bookDetailsPanel.setLayout(new BoxLayout(bookDetailsPanel, BoxLayout.Y_AXIS)); // Vertical layout for details

                    // Display book title, author, genre, price, and the date it was added to the cart
                    JLabel bookTitleLabel = new JLabel("Title: " + book.getTitle());
                    JLabel bookAuthorLabel = new JLabel("Author: " + book.getAuthor());
                    JLabel bookGenreLabel = new JLabel("Genre: " + book.getGenre());
                    JLabel bookPriceLabel = new JLabel("Price: Rs." + book.getPrice());
                    JLabel bookAddedAtLabel = new JLabel("Added to Cart on: " + addedAt);

                    // Set font sizes
                    bookTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    bookAuthorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    bookGenreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    bookPriceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    bookAddedAtLabel.setFont(new Font("Arial", Font.ITALIC, 14));

                    // Add the book details to the bookDetailsPanel
                    bookDetailsPanel.add(bookTitleLabel);
                    bookDetailsPanel.add(bookAuthorLabel);
                    bookDetailsPanel.add(bookGenreLabel);
                    bookDetailsPanel.add(bookPriceLabel);
                    bookDetailsPanel.add(bookAddedAtLabel);

                    // Add a checkbox for modifying the cart (removing items)
                    JCheckBox checkBox = new JCheckBox("Remove this item");
                    checkBox.putClientProperty("bookId", book.getId()); // Store bookId in the checkbox property
                    checkBoxList.add(checkBox);
                    checkBox.setVisible(false); // Optionally set visibility
                    bookDetailsPanel.add(checkBox); // Add the checkbox to the book details panel

                    // Add cover image and details panel to the bookPanel
                    bookPanel.add(coverImageLabel); // Add cover image to the left
                    bookPanel.add(bookDetailsPanel); // Add book details to the right

                    // Add the bookPanel to the outer booksInCart
                    booksInCart.add(bookPanel);

                    // Calculate total amount
                    totalAmount += book.getPrice().doubleValue();
                }

// Finally, add booksInCart to the cartItemsPanel
                cartItemsPanel.add(booksInCart);

                // Wrap cart items panel in a scroll pane
                JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                viewCartPanel.add(scrollPane, BorderLayout.CENTER);

                // Create a panel for total amount and buttons
                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

                // Display the total amount at the bottom
                JLabel totalAmountLabel = new JLabel("Total Amount: Rs." + totalAmount, JLabel.CENTER);
                totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 20));
                JPanel viewCart1Panel = new JPanel();
                viewCart1Panel.setLayout(new BorderLayout());
                viewCart1Panel.add(totalAmountLabel, BorderLayout.SOUTH);
                bottomPanel.add(viewCart1Panel);

                // Create a panel for buttons at the bottom
                JPanel buttonPanel = new JPanel(new FlowLayout()); // FlowLayout for buttons

                // Back button to return to the home page
                JButton backButton = new JButton("Back to Home");
                backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home"));

                // Checkout button to confirm purchase
                double[] totalAmountWrapper = { totalAmount };

                JButton checkoutButton = new JButton("Checkout");
                checkoutButton.setEnabled(!cartIsEmpty);  // Enable button only if cart is not empty
                checkoutButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(viewCartPanel,
                            "Do you want to proceed with the checkout?",
                            "Checkout Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Create a password prompt dialog
                        JPasswordField passwordField = new JPasswordField();
                        int option = JOptionPane.showConfirmDialog(
                                frame, passwordField, "Enter Password to Confirm", JOptionPane.OK_CANCEL_OPTION);

                        if (option == JOptionPane.OK_OPTION) {
                            // Get the entered password
                            String enteredPassword = new String(passwordField.getPassword());

                            // Check if the entered password matches the stored password
                            if (enteredPassword.equals(loggedInUserPassword)) { // Assuming loggedInUserPassword is the stored password
                                // Proceed to the purchase page after successful password validation
                                showPurchasePage(loggedInUserId, totalAmountWrapper[0]);
                            } else {
                                // Show error message if the password is incorrect
                                JOptionPane.showMessageDialog(frame, "Incorrect password. Please try again.");
                            }
                        }
                    }
                });

                // Modify Cart button to allow users to remove items
                JButton modifyCartButton = new JButton("Modify Cart");
                modifyCartButton.setEnabled(!cartIsEmpty);
                modifyCartButton.addActionListener(e -> {
                    for (JCheckBox checkBox : checkBoxList) {
                        checkBox.setVisible(true); // Make all checkboxes visible for selection
                    }
                });

                // Confirm button to delete selected books from the cart
                JButton confirmButton = new JButton("Confirm Changes");
                confirmButton.setEnabled(!cartIsEmpty);
                confirmButton.addActionListener(e -> {
                    java.util.List<Integer> booksToRemove = new ArrayList<>();
                    for (JCheckBox checkBox : checkBoxList) {
                        if (checkBox.isSelected()) {
                            int bookId = (int) checkBox.getClientProperty("bookId");
                            booksToRemove.add(bookId); // Collect selected book IDs
                        }
                    }

                    // Remove selected books from the cart
                    if (!booksToRemove.isEmpty()) {
                        try (Connection conna = getConnection();
                             PreparedStatement deleteStmt = conna.prepareStatement(
                                     "DELETE FROM cart WHERE user_id = ? AND book_id = ?")) {
                            for (int bookId : booksToRemove) {
                                deleteStmt.setInt(1, loggedInUserId);
                                deleteStmt.setInt(2, bookId);
                                deleteStmt.executeUpdate();
                            }
                            JOptionPane.showMessageDialog(viewCartPanel, "Selected books removed from cart.");
                            cardLayout.show(frame.getContentPane(), "Home"); // Go back to home after modifying
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(viewCartPanel, "Error removing selected books.");
                        }
                    }
                });

                // Add buttons to the button panel
                buttonPanel.add(modifyCartButton);
                buttonPanel.add(confirmButton);
                buttonPanel.add(checkoutButton);
                buttonPanel.add(backButton);

                // Add button panel to the bottom panel
                bottomPanel.add(buttonPanel);
                booksInCart.add(bottomPanel);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(viewCartPanel, "Error fetching cart details.");
        }

        return viewCartPanel;
    }

    // Method to display the Purchase Page after checkout
    private static void showPurchasePage(int userId, double totalAmount) {
        JFrame purchaseFrame = new JFrame("Purchase Summary");
        purchaseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window when the user exits it

        // Set the size of the frame to 400x300
        purchaseFrame.setSize(400, 300);

        // Center the frame on the screen
        purchaseFrame.setLocationRelativeTo(null);

        // Create the purchase panel and set its layout to BorderLayout
        JPanel purchasePanel = new JPanel(new BorderLayout());

        // Create a title panel for the top of the purchasePanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the panel

        JLabel purchaseTitle = new JLabel("Purchase Summary", JLabel.CENTER);
        purchaseTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(purchaseTitle);

        // Thank you message
        JLabel thankYouLabel = new JLabel("Thank you for your purchase!", JLabel.CENTER);
        thankYouLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(thankYouLabel);

        JLabel happyLabel = new JLabel("Happy reading!!!", JLabel.CENTER);
        happyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(happyLabel);

        // Display total amount paid
        JLabel totalPaidLabel = new JLabel("Total Amount Paid: Rs." + totalAmount, JLabel.CENTER);
        totalPaidLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(totalPaidLabel);

        // Center the title panel within the purchase panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(titlePanel);
        purchasePanel.add(centerPanel, BorderLayout.CENTER);

        // Fetch purchased book names from the database
        try (Connection connection = getConnection()) {
            // Assume you have a method to get the book IDs from the cart or purchases
            List<Integer> bookIds = getPurchasedBookIds(connection, userId); // Implement this method
            List<String> bookTitles = getBookTitles(connection, bookIds); // Implement this method

            // Display purchased book titles
            JLabel purchasedBooksLabel = new JLabel("Books Purchased:", JLabel.CENTER);
            purchasedBooksLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            titlePanel.add(purchasedBooksLabel);

            for (String title : bookTitles) {
                JLabel bookLabel = new JLabel(title, JLabel.CENTER);
                bookLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                titlePanel.add(bookLabel);
            }

            // Deduct balance and insert purchase record
            deductBalance(connection, userId, totalAmount); // Implement this method
            insertPurchaseRecords(connection, userId, bookIds); // Implement this method
            clearUserCart(connection, loggedInUserId); // Clear the cart for the user
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }

        // Create a panel for the button and add the back button
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            purchaseFrame.dispose(); // Close the purchase frame
            cardLayout.show(frame.getContentPane(), "Home"); // Show the home panel
        });
        buttonPanel.add(backButton);

        // Add the buttonPanel to the bottom of the purchasePanel
        purchasePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the purchasePanel to the frame
        purchaseFrame.getContentPane().add(purchasePanel);

        // Set the frame visible
        purchaseFrame.setVisible(true);
    }
    private static void clearUserCart(Connection connection, int loggedInUserId) {
        String query = "DELETE FROM cart WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, loggedInUserId); // Set the user_id in the query

            // Execute the deletion
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions
        }
    }

// Implement the following methods as needed:

    private static List<Integer> getPurchasedBookIds(Connection connection, int userId) {
        List<Integer> bookIds = new ArrayList<>();
        String query = "SELECT book_id FROM cart WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId); // Set the user ID parameter
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set and add book IDs to the list
            while (resultSet.next()) {
                bookIds.add(resultSet.getInt("book_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions (e.g., log the error or show a message to the user)
        }

        return bookIds; // Return the list of book IDs
    }

    private static List<String> getBookTitles(Connection connection, List<Integer> bookIds) {
        List<String> bookTitles = new ArrayList<>();

        if (bookIds.isEmpty()) {
            return bookTitles; // Return an empty list if no book IDs are provided
        }

        // Build a parameterized SQL query with placeholders
        StringBuilder query = new StringBuilder("SELECT title FROM books WHERE id IN (");
        for (int i = 0; i < bookIds.size(); i++) {
            query.append("?");
            if (i < bookIds.size() - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            // Set each book ID in the prepared statement
            for (int i = 0; i < bookIds.size(); i++) {
                preparedStatement.setInt(i + 1, bookIds.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set and add book titles to the list
            while (resultSet.next()) {
                bookTitles.add(resultSet.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions (e.g., log the error or show a message to the user)
        }

        return bookTitles; // Return the list of book titles
    }

    private static void deductBalance(Connection connection, int userId, double amount) {
        String query = "UPDATE users SET balance = balance - ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, amount); // Set the amount to deduct
            preparedStatement.setInt(2, userId); // Set the user ID

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No user found with ID: " + userId);
                // Handle case where the user does not exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions (e.g., log the error or show a message to the user)
        }
    }

    private static void insertPurchaseRecords(Connection connection, int userId, List<Integer> bookIds) {
        String query = "INSERT INTO purchases (user_id, book_id, purchase_date) VALUES (?, ?, NOW())";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int bookId : bookIds) {
                preparedStatement.setInt(1, userId); // Set the user ID
                preparedStatement.setInt(2, bookId); // Set the book ID

                // Execute the insert
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions (e.g., log the error or show a message to the user)
        }
    }

    private static JPanel creategenrepagePanel(String selectedGenre) {
        JPanel homePanel = new JPanel();
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel homeTitle = new JLabel("Welcome to the EBook Store!", JLabel.CENTER);
        homeTitle.setFont(new Font("Arial", Font.BOLD, 30));

        titlePanel.add(homeTitle);
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

        // Create the search and filter panel
        JPanel searchFilterPanel = new JPanel();
        searchFilterPanel.setLayout(new FlowLayout());

        JButton profileButton = new JButton("Profile");
        profileButton.addActionListener(e -> {
            JPanel profilePanel = createProfilePanel();
            frame.getContentPane().add(profilePanel, "Profile"); // Add profile panel to CardLayout
            cardLayout.show(frame.getContentPane(), "Profile"); // Switch to profile panel
        });

        JTextField searchField = new JTextField(20); // Search bar
        JButton searchButton = new JButton("Search");

        String[] genres = {"All", "Fiction", "Non-Fiction", "Mystery", "Science Fiction", "Fantasy", "Biography"};
        JComboBox<String> genreComboBox = new JComboBox<>(genres); // Dropdown for genres
        JButton filterButton = new JButton("Filter by Genre");

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> {
            JPanel viewCartPanel = createViewCartPanel(loggedInUserId); // Method to create view cart panel
            frame.getContentPane().add(viewCartPanel, "ViewCart"); // Add view cart panel to CardLayout
            cardLayout.show(frame.getContentPane(), "ViewCart"); // Switch to view cart panel
        });

        // Action listener for search
        searchButton.addActionListener(e -> {
            String searchText1 = searchField.getText();
            // Create the search home panel with the search text
            JPanel searchHomePanel = createSearchPanel(searchText1);

            // Remove the current panel (home panel) from the CardLayout
            frame.getContentPane().remove(homePanel); // Assuming `homePanel` is the current panel

            // Add the search home panel to the CardLayout
            frame.getContentPane().add(searchHomePanel, "SearchHome");

            // Show the search home panel
            cardLayout.show(frame.getContentPane(), "SearchHome");
        });

        // Action listener for filter
        filterButton.addActionListener(e -> {
            String selectedGenre1 = (String) genreComboBox.getSelectedItem();

            if (selectedGenre1.equals("All")) {
                // If "All" is selected, go back to the home page
                cardLayout.show(frame.getContentPane(), "Home");
            } else {
                try {
                    // Create the filtered genre panel with the selected genre
                    JPanel genrePanel = creategenrepagePanel(selectedGenre1);

                    // Remove the current panel (home panel) from the CardLayout
                    frame.getContentPane().remove(homePanel); // Assuming `homePanel` is the current panel

                    // Add the genre panel to the CardLayout
                    frame.getContentPane().add(genrePanel, "GenreFilter");

                    // Show the genre filter panel
                    cardLayout.show(frame.getContentPane(), "GenreFilter");
                } catch (Exception ex) {
                    // Handle the exception and show an error message
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred while filtering by genre.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add profile button to the search filter panel
        searchFilterPanel.add(profileButton); // Add profile button first
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(searchButton);
        searchFilterPanel.add(genreComboBox);
        searchFilterPanel.add(filterButton);
        searchFilterPanel.add(viewCartButton);

        // Add search filter panel to the home panel
        homePanel.add(titlePanel);
        homePanel.add(searchFilterPanel); // Add searchFilterPanel first

        // Add genre panels (multiple genres like Fiction, Non-Fiction, etc.)
        try {
            homePanel.add(createGenrePanel(selectedGenre));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Create the button panel (logout button)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Login"));
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home")); // Assuming "Home" is the name for the home panel
        buttonPanel.add(backButton);

        buttonPanel.add(logoutButton); // Adding the panel with the logout button

        homePanel.add(buttonPanel); // Adding the button panel

        // Wrap homePanel in a JScrollPane for scroll functionality
        JScrollPane scrollPane = new JScrollPane(homePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Vertical scrollbar when needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No horizontal scrollbar
        scrollPane.addMouseWheelListener(e -> {
            // Change the amount scrolled for each mouse wheel event
            int notches = e.getWheelRotation();
            int scrollAmount = 10; // Change this value to increase or decrease scroll speed
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getValue() + notches * scrollAmount);
        });

        // Return the scrollable panel
        JPanel searchHomePanel = new JPanel(new BorderLayout());
        searchHomePanel.add(scrollPane, BorderLayout.CENTER);

        return searchHomePanel;
    }

    private static JPanel createSearchPanel(String searchText) {
        JPanel homePanel = new JPanel();
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel homeTitle = new JLabel("Welcome to the EBook Store!", JLabel.CENTER);
        homeTitle.setFont(new Font("Arial", Font.BOLD, 30));

        titlePanel.add(homeTitle);
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

        // Create the search and filter panel
        JPanel searchFilterPanel = new JPanel();
        searchFilterPanel.setLayout(new FlowLayout());

        JButton profileButton = new JButton("Profile");
        profileButton.addActionListener(e -> {
            JPanel profilePanel = createProfilePanel();
            frame.getContentPane().add(profilePanel, "Profile"); // Add profile panel to CardLayout
            cardLayout.show(frame.getContentPane(), "Profile"); // Switch to profile panel
        });

        JTextField searchField = new JTextField(20); // Search bar
        JButton searchButton = new JButton("Search");

        String[] genres = {"All", "Fiction", "Non-Fiction", "Mystery", "Science Fiction", "Fantasy", "Biography"};
        JComboBox<String> genreComboBox = new JComboBox<>(genres); // Dropdown for genres
        JButton filterButton = new JButton("Filter by Genre");

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> {
            JPanel viewCartPanel = createViewCartPanel(loggedInUserId); // Method to create view cart panel
            frame.getContentPane().add(viewCartPanel, "ViewCart"); // Add view cart panel to CardLayout
            cardLayout.show(frame.getContentPane(), "ViewCart"); // Switch to view cart panel
        });

        // Action listener for search
        searchButton.addActionListener(e -> {
            String searchText1 = searchField.getText();
            // Create the search home panel with the search text
            JPanel searchHomePanel = createSearchPanel(searchText1);

            // Remove the current panel (home panel) from the CardLayout
            frame.getContentPane().remove(homePanel); // Assuming `homePanel` is the current panel

            // Add the search home panel to the CardLayout
            frame.getContentPane().add(searchHomePanel, "SearchHome");

            // Show the search home panel
            cardLayout.show(frame.getContentPane(), "SearchHome");
        });

        // Action listener for filter
        filterButton.addActionListener(e -> {
            String selectedGenre1 = (String) genreComboBox.getSelectedItem();

            if (selectedGenre1.equals("All")) {
                // If "All" is selected, go back to the home page
                cardLayout.show(frame.getContentPane(), "Home");
            } else {
                try {
                    // Create the filtered genre panel with the selected genre
                    JPanel genrePanel = creategenrepagePanel(selectedGenre1);

                    // Remove the current panel (home panel) from the CardLayout
                    frame.getContentPane().remove(homePanel); // Assuming `homePanel` is the current panel

                    // Add the genre panel to the CardLayout
                    frame.getContentPane().add(genrePanel, "GenreFilter");

                    // Show the genre filter panel
                    cardLayout.show(frame.getContentPane(), "GenreFilter");
                } catch (Exception ex) {
                    // Handle the exception and show an error message
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred while filtering by genre.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add profile button to the search filter panel
        searchFilterPanel.add(profileButton); // Add profile button first
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(searchButton);
        searchFilterPanel.add(genreComboBox);
        searchFilterPanel.add(filterButton);
        searchFilterPanel.add(viewCartButton);

        // Add search filter panel to the home panel
        homePanel.add(titlePanel);
        homePanel.add(searchFilterPanel); // Add searchFilterPanel first

        // Add genre panels (multiple genres like Fiction, Non-Fiction, etc.)
        try {
            homePanel.add(createSearchResultsPanel(searchText));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Create the button panel (logout button)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Login"));
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home")); // Assuming "Home" is the name for the home panel
        buttonPanel.add(backButton);

        buttonPanel.add(logoutButton); // Adding the panel with the logout button

        homePanel.add(buttonPanel); // Adding the button panel

        // Wrap homePanel in a JScrollPane for scroll functionality
        JScrollPane scrollPane = new JScrollPane(homePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Vertical scrollbar when needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No horizontal scrollbar
        scrollPane.addMouseWheelListener(e -> {
            // Change the amount scrolled for each mouse wheel event
            int notches = e.getWheelRotation();
            int scrollAmount = 10; // Change this value to increase or decrease scroll speed
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getValue() + notches * scrollAmount);
        });

        // Return the scrollable panel
        JPanel searchHomePanel = new JPanel(new BorderLayout());
        searchHomePanel.add(scrollPane, BorderLayout.CENTER);

        return searchHomePanel;
    }

    public static class Book {
        private int id;
        private String title;
        private String author;
        private String genre;
        private BigDecimal price;
        private Date publicationDate;
        private String description;
        private Date purchaseDate;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Date getPublicationDate() {
            return publicationDate;
        }

        public void setPublicationDate(Date publicationDate) {
            this.publicationDate = publicationDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public Date getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    }

    private static JPanel createSearchResultsPanel(String searchText) throws Exception {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Create a JPanel to hold the search results JLabel
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center alignment for the panel

        // Create and add the search results label
        JLabel resultsLabel = new JLabel("Search Results for: " + searchText);
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Add the results label to the labelPanel
        labelPanel.add(resultsLabel);
        resultsPanel.add(labelPanel);

        List<Book> books = new ArrayList<>(); // List to hold the Book objects

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish connection to the database
            conn = getConnection();

            // Prepare SQL query to search for books by title (using LIKE for partial match)
            String query = "SELECT id, title, author, genre, price, publication_date, description FROM books WHERE title LIKE ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchText + "%"); // Use wildcards for partial match

            // Execute the query
            rs = stmt.executeQuery();

            // Process the result set and create Book objects
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setPublicationDate(rs.getDate("publication_date"));
                book.setDescription(rs.getString("description"));

                books.add(book);
            }

            if (!hasResults) {
                JLabel noResultsLabel = new JLabel("No results found for: " + searchText);
                resultsPanel.add(noResultsLabel);
            } else {
                int booksPerPanel = 5;
                JPanel bookPanel = null;

                for (int i = 0; i < books.size(); i++) {
                    if (i % booksPerPanel == 0) {
                        // Create a new book panel every 5 books
                        bookPanel = new JPanel();
                        bookPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 115, 10)); // Set gaps as needed
                        resultsPanel.add(bookPanel); // Add the new panel to resultsPanel
                    }

                    Book book = books.get(i);

                    JPanel bookItem = new JPanel();
                    bookItem.setLayout(new BorderLayout());
                    bookItem.setPreferredSize(new Dimension(170, 280)); // Set a preferred size if needed

                    // Create a JLabel for the book cover
                    String coverImagePath = "/Users/siddheshm/Documents/Advanced OOPS/books/" + book.getId() + ".jpg"; // Adjust to your path
                    ImageIcon originalIcon = new ImageIcon(coverImagePath);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(170, 280, Image.SCALE_SMOOTH); // Scale the image
                    JLabel coverLabel = new JLabel(new ImageIcon(scaledImage));
                    bookItem.add(coverLabel, BorderLayout.CENTER);

                    // Create a JLabel for the book title
                    JLabel titleLabel = new JLabel(book.getTitle(), JLabel.CENTER);
                    titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    titleLabel.setForeground(Color.BLUE); // Make title clickable
                    titleLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            // Implement book detail view here
                            showBookDetails(book); // Passing the Book object
                        }
                    });

                    bookItem.add(titleLabel, BorderLayout.SOUTH);
                    bookPanel.add(bookItem); // Add the bookItem to the current bookPanel
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error retrieving search results.");
            resultsPanel.add(errorLabel);
        } finally {
            // Clean up and close database resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultsPanel;
    }

    private static List<Book> getBooksByGenre(String genre) throws Exception {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE genre = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, genre);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setPublicationDate(rs.getDate("publication_date"));
                book.setDescription(rs.getString("description"));
                books.add(book);
            }
        }

        return books;
    }

    private static JPanel createGenrePanel(String genre) throws Exception {
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.Y_AXIS));

// Create a JPanel to hold the genre JLabel
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center alignment for the panel

// Create and add the genre label
        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("Arial", Font.BOLD, 18));

// Add the genre label to the labelPanel
        labelPanel.add(genreLabel);

// Add the labelPanel to the genrePanel
        genrePanel.add(labelPanel);

        // Create a panel for the books using FlowLayout with gaps
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 115, 10)); // Horizontal and vertical gaps set to 10

        // Fetch books by genre
        List<Book> books = getBooksByGenre(genre);

        // Add each book to the book panel
        for (Book book : books) {
            JPanel bookItem = new JPanel();
            bookItem.setLayout(new BorderLayout());
            bookItem.setPreferredSize(new Dimension(170, 280)); // Set a preferred size if needed

            // Create a JLabel for the book cover
            String coverImagePath = "/Users/siddheshm/Documents/Advanced OOPS/books/" + book.getId() + ".jpg"; // Adjust to your path
            ImageIcon originalIcon = new ImageIcon(coverImagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(170, 280, Image.SCALE_SMOOTH); // Change dimensions as needed
            JLabel coverLabel = new JLabel(new ImageIcon(scaledImage));
            bookItem.add(coverLabel, BorderLayout.CENTER);

            // Create a JLabel for the book title
            JLabel titleLabel = new JLabel(book.getTitle(), JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            titleLabel.setForeground(Color.BLUE); // Make title clickable
            titleLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Implement book detail view here
                    showBookDetails(book);
                }
            });

            bookItem.add(titleLabel, BorderLayout.SOUTH);
            bookPanel.add(bookItem);
        }

        genrePanel.add(bookPanel); // Add the book panel below the genre label
        return genrePanel;
    }
    private static void showBookDetails(Book book) {
        // Create a new frame to display book details
        JFrame detailFrame = new JFrame(book.getTitle());
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the frame to full screen
        detailFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Make the frame full screen

        // Create the main panel with BoxLayout
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for the main panel

        // Load the book cover image
        String coverImagePath = "/Users/siddheshm/Documents/Advanced OOPS/books/" + book.getId() + ".jpg";
        ImageIcon originalIcon = new ImageIcon(coverImagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 900, Image.SCALE_SMOOTH); // Adjust size as needed
        JLabel coverLabel = new JLabel(new ImageIcon(scaledImage));

        // Create a panel for the cover image and book details
        JPanel imageInfoPanel = new JPanel();
        imageInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align components to the left

        // Create a panel for the book details
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // Increase text size for the info panel
        Font largeFont = new Font("Arial", Font.PLAIN, 16); // Adjust the font size as needed

        // Display book details
        JLabel titleLabel = new JLabel("Title: " + book.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);

        JLabel authorLabel = new JLabel("Author: " + book.getAuthor());
        authorLabel.setFont(new Font("Arial", Font.BOLD, 24));
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(authorLabel);

        JLabel genreLabel = new JLabel("Genre: " + book.getGenre());
        genreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(genreLabel);

        JLabel priceLabel = new JLabel("Price: Rs." + book.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(priceLabel);

        JLabel publicationDateLabel = new JLabel("Publication Date: " + book.getPublicationDate());
        publicationDateLabel.setFont(new Font("Arial", Font.BOLD, 24));
        publicationDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(publicationDateLabel);

        // Use JTextArea for the description to allow multi-line display
        JTextArea descriptionArea = new JTextArea("Description: " + book.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 24));
        descriptionArea.setPreferredSize(new Dimension(800, 300)); // Adjust height as needed
        infoPanel.add(descriptionArea); // Add the description area to the info panel

        // Add the cover label and info panel to the imageInfoPanel
        imageInfoPanel.add(coverLabel);
        imageInfoPanel.add(infoPanel);

        // Create buttons: Buy, Add to Cart, Back to Home, Logout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Buy button - leads to a purchase page
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                // Check if the book has already been purchased by the user
                if (isBookPurchased(conn, loggedInUserId, book.getId())) {
                    JOptionPane.showMessageDialog(detailFrame, "You have already purchased this book.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit if the book was already purchased
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(detailFrame, "Error with the database.");
            }


            int confirm = JOptionPane.showConfirmDialog(detailFrame,
                    "Do you want to proceed with the checkout?",
                    "Checkout Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Create a password prompt dialog
                JPasswordField passwordField = new JPasswordField();
                int option = JOptionPane.showConfirmDialog(
                        frame, passwordField, "Enter Password to Confirm", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    // Get the entered password
                    String enteredPassword = new String(passwordField.getPassword());

                    // Check if the entered password matches the stored password
                    if (enteredPassword.equals(loggedInUserPassword)) {
                        // Assuming 'book' is an object of a Book class with appropriate methods
                        int bookId = book.getId(); // Get the ID of the book
                        double bookPrice = book.getPrice().doubleValue(); // Convert BigDecimal to double

                        try (Connection connection = getConnection()) {
                            // Check if the book was already purchased by the user
                            if (isBookPurchased(connection, loggedInUserId, bookId)) {
                                JOptionPane.showMessageDialog(detailFrame, "You have already purchased this book.", "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Exit if already purchased
                            }

                            // Get the user's current balance
                            double userBalance = getUserBalance(connection, loggedInUserId);

                            // Check if the user has sufficient balance
                            if (userBalance < bookPrice) {
                                JOptionPane.showMessageDialog(detailFrame, "Insufficient balance to purchase this book.", "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Exit if insufficient balance
                            }

                            // Deduct the book price from the user's balance
                            deductBalance(connection, loggedInUserId, bookPrice);

                            // Insert the purchase record into the purchases table
                            insertPurchaseRecord(connection, loggedInUserId, bookId);

                            // Create a new JFrame for the purchase panel
                            JFrame purchaseFrame = new JFrame("Purchase Summary");
                            purchaseFrame.setSize(350, 300); // Set the size to 300x250
                            purchaseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window when the user exits it

                            // Create the purchase panel
                            JPanel purchasePanel = new JPanel();
                            purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));
                            purchasePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT); // Center the panel's alignment

                            // Create a JTextArea for the thank you message
                            JTextPane thankYouTextPane = new JTextPane();
                            thankYouTextPane.setContentType("text/html"); // Set content type to HTML
                            thankYouTextPane.setText("<html><body><font size='6'><b>Thank you for your purchase of: " + book.getTitle() +
                                    "</b><br>The book has been added to your collection successfully<br>Happy reading!!!</font></body></html>");
                            thankYouTextPane.setEditable(false); // Make it read-only to hide the cursor
                            thankYouTextPane.setBackground(purchasePanel.getBackground()); // Match background color
                             // Set font size to 20
                            thankYouTextPane.setAlignmentX(JComponent.CENTER_ALIGNMENT); // Center the text pane

// Add the JTextPane to a JScrollPane for better display
                            JScrollPane scrollPane = new JScrollPane(thankYouTextPane);
                            scrollPane.setPreferredSize(new Dimension(300, 250)); // Set preferred size of the scroll pane
                            scrollPane.setAlignmentX(JComponent.CENTER_ALIGNMENT); // Center the scroll pane

                            purchasePanel.add(Box.createVerticalGlue()); // Add vertical glue to center the content
                            purchasePanel.add(scrollPane); // Add the scroll pane to the panel
                            purchasePanel.add(Box.createVerticalGlue()); // Add vertical glue to center the content// Center the scroll pane

                            purchasePanel.add(Box.createVerticalGlue()); // Add vertical glue to center the content
                            purchasePanel.add(scrollPane); // Add the scroll pane to the panel
                            purchasePanel.add(Box.createVerticalGlue()); // Add vertical glue to center the content

                            // Add Back button
                            JButton backButton = new JButton("Back");
                            backButton.addActionListener(backEvent -> {
                                purchaseFrame.dispose(); // Close the purchase frame
                            });

                            // Center the Back button
                            backButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                            purchasePanel.add(backButton);

                            // Add the purchase panel to the frame
                            purchaseFrame.getContentPane().add(purchasePanel);
                            purchaseFrame.setLocationRelativeTo(null); // Center the frame on the screen
                            purchaseFrame.setVisible(true);
                            detailFrame.dispose();// Show the purchase frame

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(detailFrame, "An error occurred while processing your purchase.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

// Helper method to check if the book has already been purchased


        // Add to Cart button - Adds the book to the user's cart in the database
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                // Check if the book has already been purchased by the user
                if (isBookPurchased(conn, loggedInUserId, book.getId())) {
                    JOptionPane.showMessageDialog(detailFrame, "You have already purchased this book. Cannot add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit if the book was already purchased
                }

                // If the book is not purchased, proceed to add to cart
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO cart (user_id, book_id) VALUES (?, ?)")) {
                    stmt.setInt(1, loggedInUserId); // Assuming you have a method to get the logged-in user
                    stmt.setInt(2, book.getId());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(detailFrame, "Book added to cart!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(detailFrame, "Error adding to cart.");
            }
        });

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> {
            JPanel viewCartPanel = createViewCartPanel(loggedInUserId); // Method to create view cart panel
            frame.getContentPane().add(viewCartPanel, "ViewCart"); // Add view cart panel to CardLayout
            detailFrame.dispose();
            cardLayout.show(frame.getContentPane(), "ViewCart"); // Switch to view cart panel
        });

        // Back to Home button
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            detailFrame.dispose(); // Close the detail frame
            cardLayout.show(frame.getContentPane(), "Home"); // Show the home panel
        });

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            detailFrame.dispose();
            cardLayout.show(frame.getContentPane(), "Login");
        });
        try (Connection conn = getConnection()) {
            if (isBookPurchased(conn, loggedInUserId, book.getId())) {
                JButton readBookButton = new JButton("Read Book");

                // Set an action listener to open the book content frame when clicked
                readBookButton.addActionListener(e -> {
                    JFrame bookContentFrame = createBookContentFrame(book);
                    bookContentFrame.setVisible(true);
                });

                // Add the button to the buttonPanel
                buttonPanel.add(readBookButton);
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(detailFrame, "Error checking purchase status.");
        }




        // Add buttons to the button panel
        buttonPanel.add(buyButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);

        // Add the imageInfoPanel and button panel to the detail panel
        detailPanel.add(imageInfoPanel); // Add image and info panel
        detailPanel.add(buttonPanel); // Add button panel below image and info panel

        // Wrap detailPanel in a JScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(detailPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the scrollPane (which contains the detail panel) to the frame
        detailFrame.add(scrollPane);
        detailFrame.setVisible(true);
    }
    private static boolean isBookPurchased(Connection connection, int userId, int bookId) throws SQLException {
        String query = "SELECT COUNT(*) FROM purchases WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Return true if the count is greater than 0
                }
            }
        }
        return false; // Book not purchased
    }

    // Helper method to get the user's current balance
    private static double getUserBalance(Connection connection, int userId) throws SQLException {
        String query = "SELECT balance FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance"); // Return the user's balance
                }
            }
        }
        return 0; // Default to 0 if user not found
    }

    // Helper method to deduct balance
    private static void deductBalanceb(Connection connection, int userId, double amount) throws SQLException {
        String query = "UPDATE users SET balance = balance - ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    // Helper method to insert a purchase record
    private static void insertPurchaseRecord(Connection connection, int userId, int bookId) throws SQLException {
        String query = "INSERT INTO purchases (user_id, book_id, purchase_date) VALUES (?, ?, NOW())"; // Assuming you want to use the current date
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }
    private static JPanel createProfilePanel() {
        // Main panel for the profile
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align main panel to center

        JLabel titleLabel = new JLabel("User Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the title

        // Labels to display user info
        JLabel idLabel = new JLabel();
        JLabel usernameLabel = new JLabel();
        JLabel emailLabel = new JLabel();
        JLabel fullNameLabel = new JLabel();
        JLabel createdAtLabel = new JLabel();
        JLabel balanceLabel = new JLabel();

        // Fetch user information from the database using getConnection()
        try (Connection conn = getConnection()) {
            String query = "SELECT id, username, email, full_name, created_at,balance FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUserId); // Use the stored loggedInUserId

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Fetch data from the result set
                idLabel.setText("User ID: " + rs.getInt("id"));
                usernameLabel.setText("Username: " + rs.getString("username"));
                emailLabel.setText("Email: " + rs.getString("email"));
                fullNameLabel.setText("Full Name: " + rs.getString("full_name"));
                createdAtLabel.setText("Account Created: " + rs.getTimestamp("created_at"));
                balanceLabel.setText("Account Balance: Rs." + rs.getBigDecimal("balance"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error fetching user profile: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Center align the user info labels
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fullNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createdAtLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding the labels to the profile panel
        profilePanel.add(titleLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space
        profilePanel.add(idLabel);
        profilePanel.add(usernameLabel);
        profilePanel.add(emailLabel);
        profilePanel.add(fullNameLabel);
        profilePanel.add(createdAtLabel);
        profilePanel.add(balanceLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space

        // Back button to return to home page
        JButton backButton = new JButton("Back to Home");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the back button
        backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home"));
        profilePanel.add(backButton);

        // Panel to hold the profilePanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Center the profilePanel in the centerPanel
        centerPanel.add(profilePanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Add padding around the panel

        // Use a Container for the CardLayout to ensure centering in the full window
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.add(centerPanel); // Add the centered panel to the main container

        return mainContainer; // Return the main container
    }
    private static int authenticateUser(String username, String password) {
        try (Connection conn = getConnection()) {
            String hashedPassword = hashPassword(password);
            String query = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Use hashed password for comparison

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Return the user ID on successful login
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1; // Return -1 if login fails
    }
    private static void addComponent(JPanel panel, Component comp, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(comp, gbc);
    }
    private static Connection getConnection() throws Exception {
        // Load MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the driver is loaded
        return DriverManager.getConnection(DB_URL, USER, PASSWORD); // Establish the connection
    }
    private static boolean isValidPassword(String password) {
        return password.length() >= 8 && // Minimum length requirement
                password.matches(".*[A-Z].*") && // At least one uppercase letter
                password.matches(".*[0-9].*") && // At least one number
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"); // At least one special character
    }
    private static void handleSignup(String fullName, String email, String username, String password1, String password2) {
        // Check if passwords match
        if (!password1.equals(password2)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for spaces in username and password
        if (username.contains(" ") || password1.contains(" ") || email.contains(" ")) {
            JOptionPane.showMessageDialog(frame, "Username, email, and password cannot contain spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPassword(password1)) {
            JOptionPane.showMessageDialog(frame, "Invalid password. Must contain at least 8 characters, one uppercase letter, one number, and one special character.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proceed with signup if all checks pass
        try (Connection conn = getConnection()) {
            String hashedPassword = hashPassword(password1);
            String sql = "INSERT INTO users (full_name, email, username, password, created_at) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, username);
            pstmt.setString(4, hashedPassword); // Store hashed password
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Sign up successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String loggedInUserPassword; // Class-level variable to store the user's password

    private static void handleLogin(String username, String password) {
        // Check for spaces in username and password
        if (username.contains(" ") || password.contains(" ")) {
            JOptionPane.showMessageDialog(frame, "Username and password cannot contain spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authenticate user and retrieve user ID
        int userId = authenticateUser(username, password);
        if (userId != -1) { // If login is successful
            loggedInUserId = userId; // Store the user ID
            loggedInUserPassword = password; // Store the user password
            JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            JPanel homePanel = createHomePanel(); // Create the home panel
            frame.getContentPane().add(homePanel, "Home"); // Add home panel to CardLayout
            cardLayout.show(frame.getContentPane(), "Home"); // Navigate to Home after successful login
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    private static JPanel createViewCollectionPanel(int loggedInUserId) {
        JPanel viewCollectionPanel = new JPanel();
        viewCollectionPanel.setLayout(new BorderLayout());

        JLabel cartTitle = new JLabel("Your Collection", JLabel.CENTER);
        cartTitle.setFont(new Font("Arial", Font.BOLD, 24));
        viewCollectionPanel.add(cartTitle, BorderLayout.NORTH);

// Create a panel for cart items with a vertical BoxLayout
        JPanel cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));

        boolean collectionIsEmpty = true;
        java.util.List<JCheckBox> checkBoxList = new ArrayList<>(); // List to store checkboxes

// Query to fetch the cart details along with book information
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT b.id, b.title, b.author, b.genre, p.purchase_date " +
                             "FROM purchases p JOIN books b ON p.book_id = b.id " +
                             "WHERE p.user_id = ?")) {

            stmt.setInt(1, loggedInUserId); // Fetch cart items for the logged-in user
            ResultSet rs = stmt.executeQuery();

            // Check if the cart has items
            if (!rs.isBeforeFirst()) { // No data in cart
                JLabel emptyMessage = new JLabel("Your collection is currently empty.", JLabel.CENTER);
                viewCollectionPanel.add(emptyMessage, BorderLayout.CENTER);
                JButton backButton = new JButton("Back to Home");
                backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home"));
                viewCollectionPanel.add(backButton, BorderLayout.SOUTH);
            } else {
                collectionIsEmpty = false; // Cart has items
                JPanel booksInCart = new JPanel();
                booksInCart.setLayout(new BoxLayout(booksInCart, BoxLayout.Y_AXIS)); // Vertical stacking
                int cbooks = 0;

                while (rs.next()) {
                    cbooks++;
                    // Fetch book details and create a Book object
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setPurchaseDate(rs.getDate("purchase_date"));

                    // Create a panel for each book with FlowLayout
                    JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                    // Cover Image
                    String coverImagePath = "/Users/siddheshm/Documents/Advanced OOPS/books/" + book.getId() + ".jpg";
                    ImageIcon bookCoverIcon = new ImageIcon(coverImagePath);
                    JLabel coverImageLabel = new JLabel();
                    coverImageLabel.setIcon(new ImageIcon(bookCoverIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
                    coverImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

                    // Make the cover image clickable
                    coverImageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JFrame bookContentFrame = createBookContentFrame(book);
                            bookContentFrame.setVisible(true);
                        }
                    });

                    // Create a panel for book details
                    JPanel bookDetailsPanel = new JPanel();
                    bookDetailsPanel.setLayout(new BoxLayout(bookDetailsPanel, BoxLayout.Y_AXIS));

                    // Display book title, author, genre, and the date it was added to the cart
                    JLabel bookTitleLabel = new JLabel("Title: " + book.getTitle());
                    JLabel bookAuthorLabel = new JLabel("Author: " + book.getAuthor());
                    JLabel bookGenreLabel = new JLabel("Genre: " + book.getGenre());
                    JLabel bookAddedAtLabel = new JLabel("Added to Collection on: " + book.getPurchaseDate());

                    // Set font sizes
                    bookTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    bookAuthorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    bookGenreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    bookAddedAtLabel.setFont(new Font("Arial", Font.ITALIC, 14));

                    // Add the book details to the bookDetailsPanel
                    bookDetailsPanel.add(bookTitleLabel);
                    bookDetailsPanel.add(bookAuthorLabel);
                    bookDetailsPanel.add(bookGenreLabel);
                    bookDetailsPanel.add(bookAddedAtLabel);

                    // Add cover image and details panel to the bookPanel
                    bookPanel.add(coverImageLabel);
                    bookPanel.add(bookDetailsPanel);

                    // Add the bookPanel to the outer booksInCart
                    booksInCart.add(bookPanel);
                }

                // Finally, add booksInCart to the cartItemsPanel
                cartItemsPanel.add(booksInCart);

                // Wrap cart items panel in a scroll pane
                JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                viewCollectionPanel.add(scrollPane, BorderLayout.CENTER);

                // Create a panel for total amount and buttons at the bottom
                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

                // Display the total amount at the bottom
                JLabel totalAmountLabel = new JLabel("Number of books in collection: " + cbooks);
                totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 20));
                // Create a panel for the total amount
                JPanel totalAmountPanel = new JPanel();
                totalAmountPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center alignment

// Add the label to the panel
                totalAmountPanel.add(totalAmountLabel);

// Add the total amount panel to the bottom panel
                bottomPanel.add(totalAmountPanel);


                // Create a panel for buttons at the bottom
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JButton backButton = new JButton("Back to Home");
                backButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Home"));
                buttonPanel.add(backButton);
                bottomPanel.add(buttonPanel);

                // Add the bottomPanel to the main viewCollectionPanel
                viewCollectionPanel.add(bottomPanel, BorderLayout.SOUTH);
            }
        }

         catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(viewCollectionPanel, "Error fetching cart details.");
        }
        viewCollectionPanel.revalidate();
        viewCollectionPanel.repaint();

        return viewCollectionPanel;
    }

    public static JFrame createBookContentFrame(Book book) {
        // Create a new JFrame for displaying book content
        JFrame bookContentFrame = new JFrame(book.getTitle());
        bookContentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        bookContentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bookContentFrame.setLayout(new BorderLayout());

        // Path to the HTML file for this book
        String filePath = "/Users/siddheshm/Documents/Advanced OOPS/books/" + book.getId() + ".html";

        // Create the top panel with a back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            bookContentFrame.dispose();
        });

        topPanel.add(backButton);

        // Load and display the HTML content in a JEditorPane
        JEditorPane contentPane = new JEditorPane();
        contentPane.setEditable(false);
        contentPane.setContentType("text/html");

        try {
            contentPane.setPage(new File(filePath).toURI().toURL());
        } catch (IOException e) {
            contentPane.setText("<html><body><p>Error loading document</p></body></html>");
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(contentPane);

        // Add the top panel and scroll pane to the frame
        bookContentFrame.add(topPanel, BorderLayout.NORTH);
        bookContentFrame.add(scrollPane, BorderLayout.CENTER);

        return bookContentFrame; // Return the configured JFrame
    }
}