package m_Star_Pathfinder;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class GridDraw extends JComponent
{
	private static final long	serialVersionUID	= 1L;
	private static Grid			grid;
	private static JFrame		window;
	private static GridDraw		component;
	private static Container	container;
	private boolean				drawPaths			= false;

	public static void main(String[] args)
	{
		window = new JFrame("window");
		window.setBounds(0, 0, 700, 700);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		component = new GridDraw();
		component.setBackground(Color.WHITE);
		container = window.getContentPane();
		container.add(component);

		setupInOutMapping();

		window.setVisible(true);
	}

	public GridDraw()
	{
		super();
		init();
	}

	public static void setupInOutMapping()
	{
		UpAction upAction = new UpAction(grid, component);
		DownAction downAction = new DownAction(grid, component);
		LeftAction leftAction = new LeftAction(grid, component);
		RightAction rightAction = new RightAction(grid, component);
		RotateClockWiseAction rotateClockWiseAction = new RotateClockWiseAction(grid, component);
		RotateCounterClockWiseAction rotateCounterClockWiseAction = new RotateCounterClockWiseAction(grid, component);
		TogglePathsAction togglePathsAction = new TogglePathsAction(grid, component);

		component.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upAction");
		component.getActionMap().put("upAction", upAction);

		component.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downAction");
		component.getActionMap().put("downAction", downAction);

		component.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
		component.getActionMap().put("leftAction", leftAction);

		component.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
		component.getActionMap().put("rightAction", rightAction);

		component.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "rotateClockWiseAction");
		component.getActionMap().put("rotateClockWiseAction", rotateClockWiseAction);

		component.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "rotateCounterClockWiseAction");
		component.getActionMap().put("rotateCounterClockWiseAction", rotateCounterClockWiseAction);

		component.getInputMap().put(KeyStroke.getKeyStroke("P"), "togglePathsAction");
		component.getActionMap().put("togglePathsAction", togglePathsAction);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		// TODO Use ActionListeners to show/hide paths
		// TODO Use ActionListeners to recalculate paths
		// System.out.println("Began drawing to the screen.");
		grid.paint(g);
		// System.out.println("Lowest next to start: " +
		// grid.getLowestAdjacentSquares(grid.getFinishPoint()));

		g.setColor(new Color(255, 0, 0));

		if (drawPaths)
		{
			for (int i = 0; i < grid.getPaths().size(); i++)
			{
				grid.paintPointSet(g, grid.getPaths().get(i).getArray());
			}
		}
		// System.out.println("Finished drawing " + grid.getPaths().size() +
		// " paths to the screen.");
		// System.out.println("Each path is " +
		// grid.getPaths().get(0).getTotalDistance() + " units long.");
	}

	public void cacheGrid()
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream("grid.data");
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

			objOut.writeObject(grid);
			objOut.close();
			System.out.println("Sucessfully cached data.");
		}
		catch (Exception e1)
		{
			System.out.println("Failed to cache data.");
		}
	}

	public void togglePaths()
	{
		drawPaths = !drawPaths;
	}

	public boolean getDrawPaths()
	{
		return drawPaths;
	}

	public void loadGrid() throws Exception
	{
		System.out.println("Began reading in data file.");
		FileInputStream fileIn = new FileInputStream("grid.data");
		ObjectInputStream objIn = new ObjectInputStream(fileIn);

		Object obj = objIn.readObject();
		objIn.close();

		if (obj instanceof Grid)
		{
			grid = (Grid) obj;
		}
		System.out.println("Finished reading in data file.");
	}

	public void init()
	{
		// TODO Experiment to make loading faster
		try
		{
			loadGrid();
		}
		catch (Exception e)
		{
			System.out.println("File not found. Began generating new data.");
			grid = new Grid(16, 16, 40, 40);
			grid.setSquareContents(new Point(1, 2), SquareType.START);
			grid.setSquareContents(new Point(5, 11), SquareType.FINISH);

			grid.setSquareContents(new Point(3, 2), SquareType.HAZARD);
			grid.setSquareContents(new Point(3, 3), SquareType.HAZARD);
			grid.setSquareContents(new Point(3, 4), SquareType.HAZARD);
			grid.setSquareContents(new Point(2, 5), SquareType.HAZARD);
			grid.setSquareContents(new Point(1, 5), SquareType.HAZARD);
			grid.setSquareContents(new Point(0, 5), SquareType.HAZARD);
			grid.setSquareContents(new Point(4, 6), SquareType.HAZARD);
			grid.setSquareContents(new Point(5, 7), SquareType.HAZARD);
			grid.setSquareContents(new Point(6, 8), SquareType.HAZARD);
			grid.setSquareContents(new Point(4, 5), SquareType.HAZARD);
			grid.setSquareContents(new Point(7, 9), SquareType.HAZARD);
			grid.setSquareContents(new Point(8, 10), SquareType.HAZARD);
			grid.setSquareContents(new Point(9, 11), SquareType.HAZARD);
			grid.setSquareContents(new Point(3, 1), SquareType.HAZARD);
			System.out.println(grid.getDistance(grid.getStartPoint()));

			PathfindAI.computeDistance(grid, grid.getStartPoint());
			PathfindAI.computePaths(grid);

			System.out.println("Finished generating new data. Begin caching.");
			cacheGrid();
		}
		System.out.println("Finished initialization.");
	}
}

class UpAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public UpAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("UpAction performed!");
		grid.moveHighlightedSquare(0);
		PathfindAI.computeDistance(grid, grid.getStartPoint());
		component.repaint();
	}

}

class DownAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public DownAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("DownAction performed!");
		grid.moveHighlightedSquare(1);
		PathfindAI.computeDistance(grid, grid.getStartPoint());
		component.repaint();
	}

}

class LeftAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public LeftAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("LeftAction performed!");
		grid.moveHighlightedSquare(2);
		// PathfindAI.computeDistance(grid, grid.getStartPoint());
		component.repaint();
	}

}

class RightAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public RightAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("RightAction performed!");
		grid.moveHighlightedSquare(3);
		// PathfindAI.computeDistance(grid, grid.getStartPoint());
		component.repaint();
	}

}

class RotateClockWiseAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public RotateClockWiseAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("RotateAction performed!");
		grid.rotateHighlightedSquare(true);
		PathfindAI.computeDistance(grid, grid.getStartPoint());
		component.repaint();
	}
}

class RotateCounterClockWiseAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public RotateCounterClockWiseAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("RotateAction performed!");
		grid.rotateHighlightedSquare(false);
		PathfindAI.computeDistance(grid, grid.getStartPoint());
		component.repaint();
	}
}

class TogglePathsAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1L;
	private Grid				grid;
	private GridDraw			component;

	public TogglePathsAction(Grid grid, GridDraw component)
	{
		super();
		this.grid = grid;
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println("TogglePathsAction performed!");
		if (!component.getDrawPaths())
		{
			PathfindAI.computeDistance(grid, grid.getStartPoint());
			PathfindAI.computePaths(grid);
		}

		component.togglePaths();
		component.repaint();
	}
}