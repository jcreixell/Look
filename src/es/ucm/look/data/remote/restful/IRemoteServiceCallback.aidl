package es.ucm.look.data.remote.restful;

oneway interface IRemoteServiceCallback {
    /**
	 * Is called when to get logged
	 
	 * @param result
	 *        The Callbacks with our ID
	 * 
	 */
	void userLogIn(String result); 
    
    void sendResponse1(String result);
    
    void sendResponse2(String result);
    
    void sendResponse3(String result);
    
}
