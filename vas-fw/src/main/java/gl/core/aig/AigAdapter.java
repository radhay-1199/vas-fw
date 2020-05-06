package gl.core.aig;

import gl.core.util.LogUtil;
import java.util.concurrent.ConcurrentHashMap;

public class AigAdapter extends LogUtil
{
	private ConcurrentHashMap<String, TpsController> idMap = null;
	private TpsConfig tpsConfig = null;
	private HttpAdapter httpAdapter = null;





	public AigAdapter() {
		this.idMap = new ConcurrentHashMap<>();
		this.tpsConfig = new TpsConfig();
		this.httpAdapter = new HttpAdapter(this.tpsConfig);
		Thread thread = new Thread(this.httpAdapter, "HttpAdapter");
		thread.start();
	}






	public String initPool(int poolSize, Object worker) { return this.httpAdapter.initPool(poolSize, worker); }






	public void addTask(String id, Object data) {
		try {
			TpsController controller = this.idMap.get(id);
			if (controller != null) {
				controller.addTask(data);
			}
		}
		catch (Exception exception) {}
	}










	public void addId(String id, String subid, int tps, Object config) {
		try {
			printLog(5, "addId id=" + id + ",subid=" + subid + ",tps=" + tps);
			this.tpsConfig.addId(id, subid, tps, config);
			TpsController controller = this.idMap.get(id);
			if (controller == null) {
				printLog(5, "newid details , id=" + id + ",subid=" + subid + ",tps=" + tps);
				controller = new TpsController(id, this.httpAdapter, this.tpsConfig);
				Thread thread = new Thread(controller, "TpsController-" + id);
				thread.start();
				this.idMap.put(id, controller);
			} else {

				controller.resetTps();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}




	public void removeId(String id) {
		try {
			TpsController controller = this.idMap.get(id);
			controller.stopWorking();
			this.idMap.remove(id);
			this.httpAdapter.removeId(id);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void removeId(String id, String subid) {}
}