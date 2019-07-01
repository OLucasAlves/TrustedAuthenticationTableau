package com.lucas.tableau.controller;

import java.io.BufferedReader;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/golden")
public class TableauController {

	@GetMapping("/painel")
	public ModelAndView painel() {
		ModelAndView mv = new ModelAndView("tableau/superloja");
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			String auth = this.getTrustedTicket("192.168.0.105", user, "0.0.0.0");

			String ticket = "http://192.168.0.105/trusted/" + auth
					+ "/views/IndicadoresMundiais/Turismo?:embed=yes&:tabs=yes&:toolbar=yes";
			// http://192.168.0.105/trusted/aSqhukqzSHiFIiJlwzhR-g==:GlUwfG3InXXV0lgq_TBWc75B/views/IndicadoresMundiais/Turismo?:embed=yes&:tabs=yes&:toolbar=yes
			mv.addObject("ticket", ticket);

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// -----------------------------------------------------------
		return mv;
	}
	
	@GetMapping
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("tableau/dashboard");
	
		return mv;
	}

	private String getTrustedTicket(String wgserver, String user, String remoteAddr) throws ServletException {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		try {
			// Encode the parameters
			StringBuffer data = new StringBuffer();
			data.append(URLEncoder.encode("username", "UTF-8"));
			data.append("=");
			data.append(URLEncoder.encode(user, "UTF-8"));
			data.append("&");
			data.append(URLEncoder.encode("client_ip", "UTF-8"));
			data.append("=");
			data.append(URLEncoder.encode(remoteAddr, "UTF-8"));

			// Send the request
			URL url = new URL("http://" + wgserver + "/trusted");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data.toString());
			out.flush();

			// Read the response
			StringBuffer rsp = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				rsp.append(line);
			}

			return rsp.toString();

		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}

}
