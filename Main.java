import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {

	public static void main(String... args) throws IOException {
		int N = 6;// количество узлов
		int L = 9;//количество ребер
		int start = 4;
		int finish = 2;
		double epsila = 0.01;
		int Lmin = 2;
		int Lmax = 8;
		int amount = (int) Math.ceil(9 / (4 * Math.pow(epsila, 2)));


		ArrayList<Rib> ribs = new ArrayList<Rib>(L);
		ribs.add(new Rib(0,1));
		ribs.add(new Rib(1,2));
		ribs.add(new Rib(1,5));
		ribs.add(new Rib(0,5));
		ribs.add(new Rib(5,2));
		ribs.add(new Rib(0,4));
		ribs.add(new Rib(4,3));
		ribs.add(new Rib(5,3));
		ribs.add(new Rib(3,2));

		for (double p = 0; p <= 1; p += 0.1) {		
			int F = 0;
			int counter = 0;
			for (int i = 0; i <= amount; i++) {
				ArrayList<Integer> subgraph = rand(L, p);
				if (vectorWeight(subgraph) >= Lmax) {
					F++;
				}
				else if  (vectorWeight(subgraph) < Lmin) {
					F+=0;
				}
				else if (searchPath(ribs, subgraph, N, start, finish)) {
					F++;
					counter++;
				}
			}
			double p_average = (double)F/amount;
			double win = (double) amount/counter;
			System.out.printf("%.5f", win);
			System.out.print(" ");
		}
	}

	public static boolean searchPath(ArrayList<Rib> RibList, ArrayList<Integer> subgraph, int vNum, int start, int finish) { // возвращает true если путь есть
		int[][] graph = new int[vNum][vNum];
		int INF = Integer.MAX_VALUE / 2; // "Бесконечность"
		for (int i = 0; i < vNum; i++) {
			for (int j = 0; j < vNum; j++){
				graph[i][j]  = INF;
			}
		}

		for (int i = 0; i < subgraph.size() ; i++) {
			if(subgraph.get(i) != 0) {
				int st = RibList.get(i).getOne();
				int fin = RibList.get(i).getTwo();
				graph[st][fin] = 1;
				graph[fin][st] = 1;
			}
		}


		boolean[] used = new boolean [vNum]; // массив пометок
		int[] dist = new int [vNum]; // массив расстояния. dist[v] = минимальное_расстояние(start, v)

		Arrays.fill(dist, INF);

		dist[start] = 0; // для начальной вершины положим 0

		for (;;) {
			int v = -1;
			for (int nv = 0; nv < vNum; nv++) // перебираем вершины
				if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv])) // выбираем самую близкую непомеченную вершину
					v = nv;
			if (v == -1) break; // ближайшая вершина не найдена
			used[v] = true; // помечаем ее
			for (int nv = 0; nv < vNum; nv++)
				if (!used[nv] && graph[v][nv] < INF) // для всех непомеченных смежных
					dist[nv] = Math.min(dist[nv], dist[v] + graph[v][nv]); // улучшаем оценку расстояния (релаксация)
		}

		if (dist[finish] == INF) {
			return false;
		}
		else {
			return true;
		}
	}

	public static ArrayList<Integer> rand(int size, double p) {// формирование случайного вектора длины size с вероятностью появления "1" равной p
		ArrayList<Integer> vector = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			double a = Math.random();
			if (a >= p) vector.add(0);
			else vector.add(1);
		}
		return vector;
	}
	
	public static int vectorWeight(ArrayList<Integer> vec) {
		int counter = 0;
		for (int i = 0; i < vec.size(); i++) {
			if (vec.get(i) == 1) {
				counter++;
			}
		}
		return counter;
	}
	
}