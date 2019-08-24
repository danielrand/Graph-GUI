import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GG1756 {
	public static void main(String args[]) {
		/*try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}*/
		new GraphGUI();
	}
}

class GraphGUI extends JFrame implements ActionListener {

	private static GraphPicturePanel picture;
	private static Container cPane; 
	private static JRadioButton addVertex, addEdge, removeVertex, removeEdge, moveVertex;
	private static JButton addAllEdges, connectedComponents, showCutVertices, help;
	private static JPanel buttonPanel;

	public GraphGUI() {
		initializeGUI();
	}

	public static void addButton(JComponent b) {
		buttonPanel.add(b);
		buttonPanel.add(Box.createVerticalStrut(10));
	}

	public void initializeGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setVisible(true);
		setTitle("Graph GUI");
		setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		ButtonGroup buttonGroup = new ButtonGroup();
		cPane = getContentPane();
		addVertex = new JRadioButton("Add Vertex");
		addVertex.addActionListener(this);
		buttonGroup.add(addVertex);
		addEdge = new JRadioButton("Add Edge");
		addEdge.addActionListener(this);
		buttonGroup.add(addEdge);
		removeVertex = new JRadioButton("Remove Vertex");
		removeVertex.addActionListener(this);
		buttonGroup.add(removeVertex);
		removeEdge = new JRadioButton("Remove Edge");
		removeEdge.addActionListener(this);
		buttonGroup.add(removeEdge);
		moveVertex = new JRadioButton("Move Vertex");
		moveVertex.addActionListener(this);
		buttonGroup.add(moveVertex);
		addAllEdges = new JButton("Add All Edges");
		addAllEdges.setPreferredSize(new Dimension(100, 200));
		addAllEdges.addActionListener(this);
		connectedComponents = new JButton("Connected Components");
		connectedComponents.addActionListener(this);
		showCutVertices = new JButton("Show Cut Vertices");
		showCutVertices.addActionListener(this);
		help = new JButton("Help");
		help.addActionListener(this);
		addButton(addVertex);
		addButton(addEdge);
		addButton(removeVertex);
		addButton(removeEdge);
		addButton(moveVertex);
		addButton(addAllEdges);
		addButton(connectedComponents);
		addButton(showCutVertices);
		addButton(help);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		cPane.add(buttonPanel, BorderLayout.WEST);
		picture = new GraphPicturePanel();
		cPane.add(picture, BorderLayout.EAST);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static boolean isAddVSelected() {
		return addVertex.isSelected();
	}

	public static boolean isAddESelected() {
		return addEdge.isSelected();
	}

	public static boolean isRemoveVSelected() {
		return removeVertex.isSelected();
	}

	public static boolean isRemoveESelected() {
		return removeEdge.isSelected();
	}

	public static boolean isMoveVSelected() {
		return moveVertex.isSelected();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		picture.resetColors();
		picture.resetSelection();
		if (e.getActionCommand().equals("Add All Edges"))
			picture.addAllEdges();
		else if (e.getActionCommand().equals("Help")) {
			String help = "Adding vertices:\n\n      Select \"Add Vertex\" and"
					+ " click on the screen wherever you would like to place a vertex."
					+ "\n\nAdding edges:\n\n      Select \"Add Edges\" and select a vertex to be "
					+ "the first endpoint of an edge. Once selected, the vertex will be "
					+ "highlighted.\n      To draw edges, click on as many edges as desired "
					+ "in order to connect them with the initial vertex. To deselect a "
					+ "vertex,\n      simply click on the selected vertex.\n\nRemoving vertices"
					+ " and their associated edges:\n\n      Select \"Remove Vertex\" and"
					+ " click on a vertex.\n\nRemoving edges:\n\n      Select \"Remove Edge\" "
					+ "and click on an edge.\n\nMoving vertices:\n\n      Click on a vertex to"
					+ " select it to be moved. Then, click anywhere on the screen to move the "
					+ "vertex to that location.\n\nAdding all edges:\n\n      Click \"Add All Edges\""
					+ " in order to completely connect the graph.\n\nShowing connected components:\n\n"
					+ "      Click \"Connected Components\" and the connected components of the graph "
					+ "will be highlighted in different colors.\n\nShowing cut vertices:\n\n      "
					+ "Click \"Show Cut Vetices\" and all of the cut vertices will be highlighted.";
			JOptionPane.showMessageDialog(null, help, "Help", 1);
		} else if (e.getActionCommand().equals("Connected Components")) {
			picture.highlightConnectedComponents();
		} else if (e.getActionCommand().equals("Show Cut Vertices")) {
			picture.showCutVertices();
		}
		picture.repaint();
	}
}

class DrawableGraph {

	private ArrayList<ArrayList<Integer>> adjacencyMatrix;
	private int numVertices;

	public DrawableGraph() {
		adjacencyMatrix = new ArrayList<>();
		// locations = new ArrayList<>();
		numVertices = 0;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public void setNumVertices(int numVertices) {
		this.numVertices = numVertices;
	}

	public ArrayList<ArrayList<Integer>> getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(ArrayList<ArrayList<Integer>> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public void addVertex() {
		numVertices++;
		for (ArrayList<Integer> v : adjacencyMatrix) {
			v.add(0);
		}
		ArrayList<Integer> newVertex = new ArrayList<>();
		for (int i = 0; i < numVertices; i++) {
			newVertex.add(0);
		}
		adjacencyMatrix.add(newVertex);
	}

	public void removeVertex(int index) {
		adjacencyMatrix.remove(index);
		for (ArrayList<Integer> v : adjacencyMatrix) {
			v.remove(index);
		}
		numVertices--;
	}

	public void addEdge(int vertexA, int vertexB) {
		if (vertexA >= adjacencyMatrix.size()) {
			throw new IllegalArgumentException("Vertex A is not in the graph");
		}
		if (vertexB >= adjacencyMatrix.get(vertexA).size()) {
			throw new IllegalArgumentException("Vertex B is not in the graph");
		}
		adjacencyMatrix.get(vertexA).set(vertexB, 1);
		adjacencyMatrix.get(vertexB).set(vertexA, 1);
	}

	public void removeEdge(int vertexA, int vertexB) {
		adjacencyMatrix.get(vertexA).set(vertexB, 0);
		adjacencyMatrix.get(vertexB).set(vertexA, 0);
	}

	public ArrayList<ArrayList<Integer>> getMatrix() {
		return adjacencyMatrix;
	}
}

class DrawableVertex {
	
	private Point location;
	private Color color;

	public DrawableVertex(Point loc, Color c) {
		location = loc;
		color = c;
	}

	public void drawVertex(Graphics g) {
		g.setColor(color);
		g.fillOval((int) location.getX(), (int) location.getY(), 10, 10);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}

class DrawableEdge {

	private DrawableVertex first, second;
	private Polygon clickableRegion;
	private Color color;

	public DrawableEdge(DrawableVertex a, DrawableVertex b, Color c) {
		first = a;
		second = b;
		color = c;
		resetClickableRegion();
	}

	public void drawEdge(Graphics g) {
		g.setColor(color);
		g.drawLine((int) first.getLocation().getX() + 5, (int) first.getLocation().getY() + 5,
				(int) second.getLocation().getX() + 5, (int) second.getLocation().getY() + 5);
	}

	public DrawableVertex getFirst() {
		return first;
	}

	public void setFirst(DrawableVertex first) {
		this.first = first;
	}

	public DrawableVertex getSecond() {
		return second;
	}

	public void setSecond(DrawableVertex second) {
		this.second = second;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Polygon getClickableRegion() {
		return clickableRegion;
	}

	public void resetClickableRegion() {
		// if the vertices's x coordinates are closer together than their y
		// coordinates
		clickableRegion = new Polygon();
		if (Math.abs(first.getLocation().getX() - second.getLocation().getX()) <= Math
				.abs(first.getLocation().getY() - second.getLocation().getY())) {
			// top left
			clickableRegion.addPoint((int) first.getLocation().getX() - 2, (int) first.getLocation().getY() + 6);
			// top right
			clickableRegion.addPoint((int) first.getLocation().getX() + 12, (int) first.getLocation().getY() + 6);
			// bottom right
			clickableRegion.addPoint((int) second.getLocation().getX() + 12, (int) second.getLocation().getY() + 6);
			// bottom left
			clickableRegion.addPoint((int) second.getLocation().getX() - 2, (int) second.getLocation().getY() + 6);
		} else {
			// top left
			clickableRegion.addPoint((int) first.getLocation().getX() + 5, (int) first.getLocation().getY() - 2);
			// top right
			clickableRegion.addPoint((int) second.getLocation().getX() + 5, (int) second.getLocation().getY() - 2);
			// bottom right
			clickableRegion.addPoint((int) second.getLocation().getX() + 5, (int) second.getLocation().getY() + 12);
			// bottom left
			clickableRegion.addPoint((int) first.getLocation().getX() + 5, (int) first.getLocation().getY() + 12);
		}
	}

	@Override
	public boolean equals(Object other) {
		Point p1 = ((DrawableEdge) other).getFirst().getLocation();
		Point p2 = ((DrawableEdge) other).getSecond().getLocation();
		if (first.getLocation().equals(p1) && second.getLocation().equals(p2)
				|| first.getLocation().equals(p2) && second.getLocation().equals(p1)) {
			return true;
		}
		return false;
	}
}

class GraphPicturePanel extends JPanel implements MouseListener {

	private DrawableGraph graph;
	private ArrayList<DrawableVertex> verticesToDraw;
	private ArrayList<DrawableEdge> edgesToDraw;
	private ArrayList<ArrayList<Integer>> connectedComponents;
	private int indexOfSelected = -1;
	private int finalIndex = -1; 
	private boolean isSelected = false;

	public GraphPicturePanel() {
		this.setPreferredSize(new Dimension(1090, 600));
		verticesToDraw = new ArrayList<>();
		edgesToDraw = new ArrayList<>();
		connectedComponents = new ArrayList<>();
		graph = new DrawableGraph();
		addMouseListener(this);
	}

	public void resetColors() {
		for (DrawableEdge e : edgesToDraw)
			e.setColor(Color.black);
		for (DrawableVertex v : verticesToDraw)
			v.setColor(Color.black);
	}

	private void computeConnectedComponents(DrawableGraph g) {
		// booleans to track whether a corresponding vertex has been visiting by the algorithm
		ArrayList<Boolean> isVisited = new ArrayList<>();
		connectedComponents.clear();
		// initialize all values of isVisitedTo false
		for (int i = 0; i < g.getMatrix().size(); i++)
			isVisited.add(false);
		for (int i = 0; i < g.getMatrix().size(); i++) {
			if (!isVisited.get(i)) {
				connectedComponents.add(new ArrayList<Integer>());
				trace(g, i, isVisited);
			}
		}
	}

	private void trace(DrawableGraph g, int index, ArrayList<Boolean> isVisited) {
		isVisited.set(index, true);
		connectedComponents.get(connectedComponents.size() - 1).add(index);
		for (int i = 0; i < g.getMatrix().get(index).size(); i++) {
			if (!isVisited.get(i) && g.getMatrix().get(index).get(i) == 1) {
				trace(g, i, isVisited);
			}
		}
	}

	public void highlightConnectedComponents() {
		computeConnectedComponents(graph);
		Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.ORANGE };
		for (ArrayList<Integer> a : connectedComponents) {
			Color color;
			if (connectedComponents.indexOf(a) >= colors.length) {
				// create random color for edges in connected components
				color = new Color((int) (Math.random() * 0x1000000));
			} else
				color = colors[connectedComponents.indexOf(a)];
			color.darker();
			for (DrawableEdge edge : edgesToDraw) {
				DrawableVertex firstV = edge.getFirst();
				DrawableVertex secondV = edge.getSecond();
				int first = verticesToDraw.indexOf(firstV);
				int second = verticesToDraw.indexOf(secondV);
				if (a.contains(first) || a.contains(second))
					edge.setColor(color);
			}
		}
	}

	public void showCutVertices() {
		computeConnectedComponents(graph);
		int numComponents = connectedComponents.size();
		for (int i = 0; i < graph.getMatrix().size(); i++) {
			DrawableGraph copy = createCopy(graph);
			int copyComponents;
			copy.removeVertex(i);
			computeConnectedComponents(copy);
			copyComponents = connectedComponents.size();
			if (copyComponents > numComponents) {
				verticesToDraw.get(i).setColor(Color.red);
			}
		}
	}

	private DrawableGraph createCopy(DrawableGraph g) {
		DrawableGraph copy = new DrawableGraph();
		ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
		for (int a = 0; a < g.getAdjacencyMatrix().size(); a++) {
			adj.add(new ArrayList<Integer>());
			for (Integer i : g.getAdjacencyMatrix().get(a)) {
				adj.get(a).add(i);
			}
		}
		copy.setAdjacencyMatrix(adj);
		copy.setNumVertices(adj.size());
		return copy;
	}

	public DrawableGraph getGraph() {
		return graph;
	}

	public void addAllEdges() {
		for (int v1 = 0; v1 < verticesToDraw.size(); v1++) {
			for (int v2 = v1; v2 < verticesToDraw.size(); v2++) {
				if (v1 != v2) {
					DrawableVertex first = verticesToDraw.get(v1);
					DrawableVertex second = verticesToDraw.get(v2);
					edgesToDraw.add(new DrawableEdge(first, second, Color.BLACK));
					graph.getMatrix().get(v1).set(v2, 1);
					graph.getMatrix().get(v2).set(v1, 1);

				}
			}
		}
	}

	public void resetSelection() {
		if (isSelected) {
			isSelected = false;
			verticesToDraw.get(indexOfSelected).setColor(Color.BLACK);
			indexOfSelected = -1;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DrawableEdge e : edgesToDraw) 
			e.drawEdge(g);
		for (DrawableVertex v : verticesToDraw)
			v.drawVertex(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (GraphGUI.isAddVSelected()) {
			Point point = new Point(e.getX() - 5, e.getY() - 7);
			verticesToDraw.add(new DrawableVertex(point, Color.BLACK));
			graph.addVertex();
		} else if (GraphGUI.isRemoveVSelected()) {
			for (int i = 0; i < verticesToDraw.size(); i++) {
				Point p = verticesToDraw.get(i).getLocation();
				int oldX = (int) p.getX();
				int oldY = (int) p.getY();
				int newX = e.getX() - 5;
				int newY = e.getY() - 7;
				// If a vertex is clicked
				if (Math.abs(newX - oldX) <= 7 && Math.abs(newY - oldY) <= 7) {
					DrawableVertex removedVertex = verticesToDraw.get(i);
					// list of edges that are associated with the removed vertex
					// that need to be deleted
					ArrayList<DrawableEdge> edgesToRemove = new ArrayList<>();
					// fill edgesToRemove
					for (DrawableEdge edge : edgesToDraw) {
						DrawableVertex firstV = edge.getFirst();
						DrawableVertex secondV = edge.getSecond();
						if (firstV.equals(removedVertex) || secondV.equals(removedVertex))
							edgesToRemove.add(edge);
					}
					for (DrawableEdge edge : edgesToRemove)
						edgesToDraw.remove(edge);
					verticesToDraw.remove(i);
					graph.removeVertex(i);
					break;
				}
			}
		} else if (GraphGUI.isMoveVSelected()) {
			boolean clickOnPoint = false;
			for (int i = 0; i < verticesToDraw.size(); i++) {
				DrawableVertex currentVertex = verticesToDraw.get(i);
				Point p = currentVertex.getLocation();
				int oldX = (int) p.getX();
				int oldY = (int) p.getY();
				int newX = e.getX() - 5;
				int newY = e.getY() - 7;
				if (Math.abs(newX - oldX) <= 7 && Math.abs(newY - oldY) <= 7) {
					clickOnPoint = true;
					if (!isSelected) {
						currentVertex.setColor(Color.RED);
						isSelected = true;
						indexOfSelected = i;
					} else if (i == indexOfSelected) {
						currentVertex.setColor(Color.BLACK);
						isSelected = false;
						indexOfSelected = -1;
					}
					break;
				}
			}
			if (isSelected && !clickOnPoint) {
				DrawableVertex currentVertex = verticesToDraw.get(indexOfSelected);
				currentVertex.setColor(Color.BLACK);				// Move vertex to new location on screen
				currentVertex.setLocation(new Point(e.getX() - 5, e.getY() - 5));
				// Loop through edges connected to the vertex, adjusting their
				// clickable regions as necessary
				for (DrawableEdge edge : edgesToDraw) {
					if (edge.getFirst().equals(currentVertex) || edge.getSecond().equals(currentVertex))
						edge.resetClickableRegion();
				}
				isSelected = false;
				indexOfSelected = -1;
			}
		} else if (GraphGUI.isAddESelected()) {
			for (int i = 0; i < verticesToDraw.size(); i++) {
				DrawableVertex currentVertex = verticesToDraw.get(i);
				Point p = currentVertex.getLocation();
				int oldX = (int) p.getX();
				int oldY = (int) p.getY();
				int newX = e.getX() - 5;
				int newY = e.getY() - 7;
				if (Math.abs(newX - oldX) <= 7 && Math.abs(newY - oldY) <= 7) {
					if (!isSelected) {
						currentVertex.setColor(Color.RED);
						isSelected = true;
						indexOfSelected = i;
					} else if (i == indexOfSelected) {
						currentVertex.setColor(Color.BLACK);
						isSelected = false;
						indexOfSelected = -1;
					} else
						finalIndex = i;
					break;
				}
			}
			if (isSelected && finalIndex != -1) {
				DrawableVertex currentVertex = verticesToDraw.get(indexOfSelected);
				DrawableVertex finalVertex = verticesToDraw.get(finalIndex);
				graph.addEdge(indexOfSelected, finalIndex);
				DrawableEdge newEdge = new DrawableEdge(currentVertex, finalVertex, Color.BLACK);
				if (!edgesToDraw.contains(newEdge))
					edgesToDraw.add(new DrawableEdge(currentVertex, finalVertex, Color.BLACK));
				finalIndex = -1;
			}
		} else if (GraphGUI.isRemoveESelected()) {
			Point p = e.getPoint();
			for (int i = 0; i < edgesToDraw.size(); i++) {
				DrawableEdge currentEdge = edgesToDraw.get(i);
				if (currentEdge.getClickableRegion().contains(p)) {
					// remove edge from screen
					DrawableVertex firstV = currentEdge.getFirst();
					DrawableVertex secondV = currentEdge.getSecond();
					int first = verticesToDraw.indexOf(firstV);
					int second = verticesToDraw.indexOf(secondV);
					edgesToDraw.remove(i);
					graph.removeEdge(first, second);
				} 
			}
		}
		repaint();
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
