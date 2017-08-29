package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import utilities.command.CookieCommand;
import cookies.JNACrypt32Utils;
import cookies.SQLiteCookie;
import cookies.SQLiteCookieReader;


/**
 * Get a web file.
 */
public final class WebFile {
	// Saved response.
	private java.util.Map<String,java.util.List<String>> responseHeader = null;
	private java.net.URL responseURL = null;
	private int responseCode = -1;
	private String MIMEtype  = null;
	private String charset   = null;
	private Object content   = null;

	/** Open a web file. */
	public WebFile( String urlString ) throws java.net.MalformedURLException, java.io.IOException {
		// Open a URL connection.
		final java.net.URL url = new java.net.URL( urlString );
		final java.net.URLConnection uconn = url.openConnection( );

		if (CookieCommand.isUseCookies()) {
			String cookieMapKey = "";
			Map<String, Map<String, SQLiteCookie>> cookieMap = SQLiteCookieReader.getSQLiteCookies();
			for (String cookieMapKeyTmp : cookieMap.keySet()) {
				if (urlString.contains(cookieMapKeyTmp) || urlString.contains(cookieMapKeyTmp.substring(1))) {
					cookieMapKey = cookieMapKeyTmp;
					break;
				}
			}

			if (!cookieMapKey.isEmpty()) {
				//			String[] cookieNames = {"dtr",
				//					"_ga",
				//					"lu",
				//					"p",
				//					"act",
				//					"c_user",
				//					"s",
				//					"csm",
				//					"xs",
				//					"fr",
				//			"presence"};

				Map<String, SQLiteCookie> subCookieMap = cookieMap.get(cookieMapKey);
				String cookie = "";
				for (String cookieName : subCookieMap.keySet()) {			
					SQLiteCookie sQLiteCookie = subCookieMap.get(cookieName);
					if (sQLiteCookie != null) {
						if (sQLiteCookie.getValue() != null && !sQLiteCookie.getValue().isEmpty()) {
							if (!cookie.isEmpty())
								cookie += "; ";					
							cookie += cookieName + "=" + sQLiteCookie.getValue();
						}
						else if (sQLiteCookie.getEncrypted_value() != null) {
							if (!cookie.isEmpty())
								cookie += "; ";					
							try {
								cookie += cookieName + "=" + new String(JNACrypt32Utils.unprotect(sQLiteCookie.getEncrypted_value()));
							} catch (Exception e) {
								e.printStackTrace();
							}					
						}
					}
				}
				if (!cookie.isEmpty())
					uconn.setRequestProperty("Cookie", cookie);
			}
		}

		if ( !(uconn instanceof java.net.HttpURLConnection) )
			throw new java.lang.IllegalArgumentException(
					"URL protocol must be HTTP." );
		final java.net.HttpURLConnection conn =
				(java.net.HttpURLConnection)uconn;

		// Set up a request.
		conn.setConnectTimeout( 10000 );    // 10 sec
		conn.setReadTimeout( 10000 );       // 10 sec
		conn.setInstanceFollowRedirects( true );
//		conn.setRequestProperty( "User-agent", "spider" );

		// Send the request.
		conn.connect( );

		// Get the response.
		responseHeader    = conn.getHeaderFields( );
		responseCode      = conn.getResponseCode( );
		responseURL       = conn.getURL( );
		final int length  = conn.getContentLength( );
		final String type = conn.getContentType( );
		if ( type != null ) {
			final String[] parts = type.split( ";" );
			MIMEtype = parts[0].trim( );
			for ( int i = 1; i < parts.length && charset == null; i++ ) {
				final String t  = parts[i].trim( );
				final int index = t.toLowerCase( ).indexOf( "charset=" );
				if ( index != -1 )
					charset = t.substring( index+8 );
			}
			// Get the content.
			final java.io.InputStream stream = conn.getErrorStream( );
			if ( stream != null )
				content = readStream( length, stream );
			else if ( (content = conn.getContent( )) != null &&
					content instanceof java.io.InputStream )
				content = readStream( length, (java.io.InputStream)content );
			conn.disconnect( );
		}
	}
	/** Read stream bytes and transcode. */
	private Object readStream( int length, java.io.InputStream stream )
			throws java.io.IOException {
		final int buflen = Math.max( 1024, Math.max( length, stream.available() ) );
		byte[] buf   = new byte[buflen];;
		byte[] bytes = null;

		for ( int nRead = stream.read(buf); nRead != -1; nRead = stream.read(buf) ) {
			if ( bytes == null ) {
				bytes = buf;
				buf   = new byte[buflen];
				continue;
			}
			final byte[] newBytes = new byte[ bytes.length + nRead ];
			System.arraycopy( bytes, 0, newBytes, 0, bytes.length );
			System.arraycopy( buf, 0, newBytes, bytes.length, nRead );
			bytes = newBytes;
		}

		if ( charset == null )
			return bytes;
		try {
			return bytes != null ? new String( bytes, charset ) : null;
		}
		catch ( java.io.UnsupportedEncodingException e ) { }
		return bytes;
	}
	
	private static StringBuffer doPowerShellKungFu(String scriptPath) {
		StringBuffer sb = new StringBuffer();

		Process proc = null;
		BufferedReader reader = null;

		try {
			Runtime runtime = Runtime.getRuntime();
			proc = runtime.exec("powershell " + scriptPath); 
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}        
		} catch (IOException e) {
			// do nothing
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (proc != null)
					proc.getOutputStream().close();
			} catch (IOException e) {
				// do nothing
			}
		}

		return sb;
	}

	/** Get the content. */
	public Object getContent( ) {
		return content;
	}

	/** Get the response code. */
	public int getResponseCode( ) {
		return responseCode;
	}

	/** Get the response header. */
	public java.util.Map<String,java.util.List<String>> getHeaderFields( ) {
		return responseHeader;
	}

	/** Get the URL of the received page. */
	public java.net.URL getURL( ) {
		return responseURL;
	}

	/** Get the MIME type. */
	public String getMIMEType( ) {
		return MIMEtype;
	}
}
