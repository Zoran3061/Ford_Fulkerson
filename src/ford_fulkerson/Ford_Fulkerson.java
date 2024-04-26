/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ford_fulkerson;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.*;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


/**
 *
 * @author Zoran
 */
public class Ford_Fulkerson 
{
	public static void main(String[] args)
        {
		
		int[][] network={
                    {0, 16, 13, 0, 0, 0},
	            {0, 0, 0, 12, 0, 0},
	            {0, 4, 0, 0, 14, 0},
	            {0, 0, 9, 0, 0, 20},
	            {0, 0, 0, 7, 0, 5},
	            {0, 0, 0, 0, 0, 0}
                };
		
		int size=network.length;
		int[][] flow=new int[size][size];
		for(int u=0;u<flow.length;u++)
                {
			for(int v=0;v<flow.length;v++)
                        {
				flow[u][v]=0;
			}
		}
		
		ArrayList<Integer> path=new ArrayList<Integer>();
		int maxflow=0;
		path=dfs(residual(network, flow));
		while(path.get(0)!=-1)
                {
			for(int i=0;i<path.size()-1;i++)
                        {
				int u=path.get(i);
				int v=path.get(i+1);
				
				if(network[u][v]>0)
					flow[u][v]++;
				else if(network[v][u]>0)
					flow[u][v]--;
					if(flow[u][v]<0)
						flow[u][v]=1;
			}
			maxflow++;
			try{
				path=dfs(residual(network, flow));
			} catch(Exception e){
				break;
			}
			
		}
		System.out.println(maxflow+"\n\n");
		for(int[] flo:flow)
                {
			System.out.println(Arrays.toString(flo));
		}

		display(network, "Ford Fulkerson");
	}
        
	 public static ArrayList<Integer> dfs(int[][] residual)
         {
		 int size=residual.length;
		 boolean[] visited=new boolean[size];
		 Arrays.fill(visited, false);
		 ArrayList<Integer> path=new ArrayList<Integer>();
		 path.add(0);
		
		 while(!visited[size-1])
                 {
			 for(int u=0;u<size;u++)
                         {
				 if(residual[path.get(path.size()-1)][u]>0 && !visited[u])
                                 {
					 path.add(u);
					 visited[u]=true;
					 u=0;
					 continue;
			 	}
			 }
			 if(path.get(path.size()-1)==(size-1))
                         {
				 return path;
			 }
			 path.remove(path.size()-1);
		 }
		 
		 if(visited[size-1])
                 {
			 return path;
		 }
		 
		 path.set(0, -1);
		 return path;
	 }
	 

	public static int[][] residual(int[][] network, int[][] flow)
        {
		int[][] residual=new int[network.length][network.length];

		for(int u=0;u<flow.length;u++)
                {
			for(int v=0;v<flow.length;v++)
                        {
				if(network[u][v]>0)
					residual[u][v]=network[u][v]-flow[u][v];
				else if(network[v][u]>0)
					residual[u][v]=flow[v][u];
				else
					residual[u][v]=0;
			}
		}
			
		return residual;
	}
	
	
	public static void display(int[][] adj, String name)
        {
		Graph<Integer,Integer> graph=new SparseMultigraph<Integer,Integer>();
		for(int i=0;i<adj.length;i++){
			graph.addVertex(i);
			for(int j=0;j<adj[0].length;j++){
				if(adj[i][j]>0)
					graph.addEdge(adj[i][j], i, j, EdgeType.DIRECTED);
			}
		}
		 
        Layout<Integer, String> layout = new CircleLayout(graph);
		layout.setSize(new Dimension(620,620));
        VisualizationViewer<Integer,String> vs = new VisualizationViewer<Integer,String>(layout);
        vs.setPreferredSize(new Dimension(650,650));
        
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vs.setGraphMouse(gm);
		
		JFrame frame = new JFrame(name);
	    frame.getContentPane().setBackground(Color.RED);
	    frame.getContentPane().add(vs);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
	 
       Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() 
       {
           public Paint transform(Integer i) 
           {
        	   return Color.GREEN;
           } 
       };

	    float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer =new Transformer<String, Stroke>(){
            public Stroke transform(String s) 
            {
                return edgeStroke;
            }
        };
        
        vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

	}
}
