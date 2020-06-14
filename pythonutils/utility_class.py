import os
import shutil
import tarfile
import xml.dom.minidom
from pathlib import Path
from xml.etree import ElementTree
from xml.dom.minidom import parse

import paramiko

from sftpNode import MySFTPClient


class utilityclass():
    def __init__(self):
        pass

    @staticmethod
    def add_node(property_name, property_value, root):
        """
        This function is used to create a configuration node in xml. It accepts property name and value as parameter along with root node
        :param property_name:
        :param property_value:
        :param root: 
        :return: 
        """
        prop = ElementTree.Element("property")
        name = ElementTree.Element("name")
        name.text = property_name
        value = ElementTree.Element("value")
        value.text = property_value
        prop.append(name)
        prop.append(value)
        root.append(prop)

    @staticmethod
    def pretty_print_file(filename):
        """
        This method is used to format the string
        :param filename: 
        :return: 
        """
        dom = xml.dom.minidom.parse(filename)
        with open(filename, 'w') as file:
            file.write(dom.toprettyxml())

    @staticmethod
    def replace_string(filename, text_to_search, replacement_text):
        """
        This method is use to search and replace the string it accepts parameters as :
        :param filename:
        :param text_to_search:
        :param replacement_text:
        """
        with open(filename, 'r') as file:
            filedata = file.read()
        # Replace the target string
        filedata = filedata.replace(text_to_search, replacement_text)
        # Write the file out again
        with open(filename, 'w') as file:
            file.write(filedata)

    # Common method to extract tar files parameter includes source directory and destination directory

    @staticmethod
    def extracter(file_name, extraction_path):
        """
        Common method to extract tar files parameter includes source directory and destination directory
        :param file_name:
        :param extraction_path:
        """
        if not file_name.exists():
            print("Oops, file doesn't exist!")
        else:
            if tarfile.is_tarfile(file_name):
                tf = tarfile.open(file_name, 'r:*')
                tf.extractall(extraction_path)
                tf.close()

    @staticmethod
    def update_node_value(file, property_name, property_value):
        """
        method is used for updating the node value in xml file.
        :param file:
        :param property_name:
        :param property_value:
        :return:
        """
        dom = parse(file)
        for element in dom.getElementsByTagName('name'):
            if element.firstChild.data == property_name:
                sibbling = element.parentNode.getElementsByTagName('value')[0]
                sibbling.firstChild.data = property_value

        with open(file, "w") as xml_file:
            dom.writexml(xml_file)

    @staticmethod
    def line_update(file, linetoberemoved, newline):
        """
        method will create a bkp file and remove the specified keyword line and append the same updated line at the end of the file.
        :param file: 
        :param linetoberemoved: 
        :param newline: 
        :return: 
        """
        shutil.copy(file, Path(file + '.bkp'))
        with open(Path(file + '.bkp')) as oldfile, open(file, 'w') as newfile:
            for line in oldfile:
                if not any(word in line for word in linetoberemoved):
                    newfile.write(line)
        os.remove(Path(file + '.bkp'))
        with open(file, "a+") as f1:
            f1.write(newline)

    @staticmethod
    def send_command(host, username, password, command):
        """
        method is used to create a ssh connection to remote machine and execute the specified file.
        stdin, stdout, stderr variable contains standard input to terminal, standard output of executed command
        and if any error is logged it is stored into the stderr respectively.
        :param host:
        :param username:
        :param password:
        :param command:
        :return:
        """
        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        ssh.connect(host, port=22, username=username, password=password)
        count = 0
        while count <= 5:
            try:
                stdin, stdout, stderr = ssh.exec_command(command=command, timeout=5)
                print(stdout.readlines())
                break
            except Exception as e:
                print(e)
                count += 1

    @staticmethod
    def upload_directory(machine, username, password, target_path, local_path):
        """
        function is used for upload a directory structure on remote machine.If directory exist then it will override
        the files. Else it will create directory structure
         :param machine:
         :param username:
         :param password:
         :param target_path:
         :param local_path:
         :return:
        """
        transport = paramiko.Transport((machine, 22))
        transport.connect(username=username, password=password)
        sftp = MySFTPClient.from_transport(transport)
        sftp.mkdir(target_path, ignore_existing=True)
        sftp.put_dir(local_path, target_path)
        sftp.close()

    @staticmethod
    def extract(extracted_path, source_dir):
        # ls command on source directory to read all the filename
        listingDirectory = next(os.walk(source_dir))[2]
        for i in listingDirectory:
            print('Extracting :' + i)
            temp = Path(os.path.join(str(source_dir), i))
            utilityclass.extracter(temp, extracted_path)
