/**
 * Copyright (c) 2016-2017, the original author or authors (dysd_2016@163.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dysd.util.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML解析帮助类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class XmlHelper {

	/**
	 * 根据名称获取子元素列表
	 * @param ele
	 * @param childEleNames
	 * @return
	 */
	public static List<Element> getChildElementsByTagName(Element ele, String... childEleNames) {
		List<String> childEleNameList = Arrays.asList(childEleNames);
		NodeList nl = ele.getChildNodes();
		List<Element> childEles = new ArrayList<Element>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && nodeNameMatch(node, childEleNameList)) {
				childEles.add((Element) node);
			}
		}
		return childEles;
	}

	/**
	 * 根据元素名获取子元素列表
	 * @param ele
	 * @param childEleName
	 * @return
	 */
	public static List<Element> getChildElementsByTagName(Element ele, String childEleName) {
		return getChildElementsByTagName(ele, new String[] {childEleName});
	}

	/**
	 * 根据元素名获取第一个子元素
	 * @param ele
	 * @param childEleName
	 * @return
	 */
	public static Element getChildElementByTagName(Element ele, String childEleName) {
		NodeList nl = ele.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && nodeNameEquals(node, childEleName)) {
				return (Element) node;
			}
		}
		return null;
	}

	/**
	 * 根据元素名获取第一个子元素的文本值
	 * @param ele
	 * @param childEleName
	 * @return
	 */
	public static String getChildElementValueByTagName(Element ele, String childEleName) {
		Element child = getChildElementByTagName(ele, childEleName);
		return (child != null ? getTextValue(child) : null);
	}

	/**
	 * 获取所有子元素
	 * @param ele
	 * @return
	 */
	public static List<Element> getChildElements(Element ele) {
		NodeList nl = ele.getChildNodes();
		List<Element> childEles = new ArrayList<Element>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element) {
				childEles.add((Element) node);
			}
		}
		return childEles;
	}

	/**
	 * 获取元素的文本值
	 * @param valueEle
	 * @return
	 */
	public static String getTextValue(Element valueEle) {
		StringBuilder sb = new StringBuilder();
		NodeList nl = valueEle.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
				sb.append(item.getNodeValue());
			}
		}
		return sb.toString();
	}

	/**
	 * 获取节点的命名空间
	 * @param node
	 * @return
	 */
	public static String getNamespaceURI(Node node) {
		return node.getNamespaceURI();
	}

	/**
	 * 获取节点的本地节点名（去除命名空间前缀后的名称）
	 * @param node
	 * @return
	 */
	public static String getLocalName(Node node) {
		return node.getLocalName();
	}

	/**
	 * 忽略命名空间的情况下，节点和名称是否匹配
	 * @param node
	 * @param desiredName
	 * @return
	 */
	public static boolean nodeNameEquals(Node node, String desiredName) {
		return desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node));
	}
	
	/**
	 * 忽略命名空间的情况下，节点名是否为预期名称的其中一个
	 * @param node
	 * @param desiredNames
	 * @return
	 */
	public static boolean nodeNameMatch(Node node, Collection<String> desiredNames) {
		return (desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName()));
	}
}
